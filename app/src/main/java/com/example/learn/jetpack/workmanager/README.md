# WorkManager

使用 WorkManager API 可以轻松地调度即使在应用退出或设备重启时仍应运行的可延迟异步任务。

## 参考文章

- 官网：<https://developer.android.com/topic/libraries/architecture/workmanager>
- Android Jetpack架构组件之WorkManager入门：<https://www.jianshu.com/p/38f2dcb2dc12>

## 源码浅析

### WorkManager初始化

WorkManager的初始化工作是放在ContentProvider的onCreate函数中的，这里先讲下ContentProvider和Application的onCreate执行时机：

注意：ContentProvider的onCreate初始化时机在Application.onCreate之前，相关源码如下：
```java
//Android SDK版本Android29
//所在类：android.app.ActivityThread
//
@UnsupportedAppUsage
private void handleBindApplication(AppBindData data) {
    //省略一些内容
    // don't bring up providers in restricted mode; they may depend on the
    // app's custom Application class
    if (!data.restrictedBackupMode) {
        if (!ArrayUtils.isEmpty(data.providers)) {
            installContentProviders(app, data.providers); //此处最终会回调到ContentProvider的onCreate函数
        }
    }
    //省略一些内容
    try {
        mInstrumentation.callApplicationOnCreate(app); //此处最终会回调到Application的onCreate函数
    } catch (Exception e) {
        if (!mInstrumentation.onException(app, e)) {
            throw new RuntimeException(
                    "Unable to create application " + app.getClass().getName()
                            + ": " + e.toString(), e);
        }
    }
    //省略一些内容
}
```

WorkManager初始化流程：`ActivityThread.handleBindApplication -> ActivityThread.installContentProviders ->
ActivityThread.installProvider -> ContentProvider.onCreate -> WorkManagerInitializer.onCreate ->
WorkManager.initialize -> WorkManagerImpl.initialize(该函数内部通过单例模式创建了WorkManagerImpl实例)`

WorkManagerImpl.initialize内部实现：
```java
//所在类androidx.work.impl.WorkManagerImpl
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public static void initialize(@NonNull Context context, @NonNull Configuration configuration) {
    synchronized (sLock) {
        //省略一些内容
        if (sDelegatedInstance == null) {
            context = context.getApplicationContext();
            if (sDefaultInstance == null) {
                sDefaultInstance = new WorkManagerImpl(
                        context,
                        configuration,
                        new WorkManagerTaskExecutor(configuration.getTaskExecutor()));
            }
            sDelegatedInstance = sDefaultInstance;
        }
    }
}
```

其中`configuration.getTaskExecutor()`函数内部会获取默认线程池，该线程池在WorkManagerInitializer类执行onCreate函数的时候被创建：
```java
//所在类androidx.work.Configuration
private @NonNull Executor createDefaultExecutor() {
    return Executors.newFixedThreadPool(
        // This value is the same as the core pool size for AsyncTask#THREAD_POOL_EXECUTOR.
        Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4)));
}
```

最后再讲讲WorkManagerImpl类的构造函数
```java
//所在类androidx.work.impl.WorkManagerImpl
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public WorkManagerImpl(
        @NonNull Context context,
        @NonNull Configuration configuration,
        @NonNull TaskExecutor workTaskExecutor,
        @NonNull WorkDatabase database) { //database使用Room创建，保存任务线程信息，主要用于App重启时保证任务可以继续执行。
    Context applicationContext = context.getApplicationContext();
    Logger.setLogger(new Logger.LogcatLogger(configuration.getMinimumLoggingLevel()));
    //有三种Scheduler：SystemJobScheduler(需要SDK版本>=23)、SystemAlarmScheduler(需要SDK版本<23)、GreedyScheduler(无SDK版本限制)
    List<Scheduler> schedulers =
            createSchedulers(applicationContext, configuration, workTaskExecutor);
    Processor processor = new Processor(
            context,
            configuration,
            workTaskExecutor,
            database,
            schedulers);
    //启动ForceStopRunnable，在APP启动时检查APP是之前否强制停止退出或有未执行完的任务，是的话重启WorkManager，保证任务可以继续执行。
    internalInit(context, configuration, workTaskExecutor, database, schedulers, processor);
}
```
