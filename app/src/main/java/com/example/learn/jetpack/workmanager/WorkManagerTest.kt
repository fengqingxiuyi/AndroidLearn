package com.example.learn.jetpack.workmanager

import android.content.Context
import androidx.work.*
import com.example.log.LogUtil
import java.util.concurrent.TimeUnit

/**
 * @author fqxyi
 * @date 2020/8/18
 */
object WorkManagerTest {

    fun testWorkManager(context: Context) {
        LogUtil.i("Worker", "testWorkManager start")
        //构建一次性请求
        //构建约束
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) //指定需要在有网的情况下
            .setRequiresBatteryNotLow(true)//指定电量在可接受范围内运行
            .setRequiresStorageNotLow(true)//指定在存储量在可接受范围内运行
            .setRequiresCharging(true)//当设备处于充电状态时运行
            .build()
        val request = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(constraints) //添加约束
            .setInitialDelay(3, TimeUnit.SECONDS) //延迟3秒执行
            //BackoffPolicy有两个值LINEAR(每次重试的时间线性增加，比如第一次10分钟，第二次就是20分钟)、EXPONENTIAL(每次重试时间指数增加)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS) //指定重试间隔时长，Worker返回Result.retry()时生效
            .setInputData(
                Data.Builder()
                    .putString("RequestType", "First Request")
                    .build()
            ) //传入参数
            .addTag("tag") //设定请求标记
            .build()
        val request2 = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(
                Data.Builder()
                    .putString("RequestType", "Second Request")
                    .build()
            ) //传入参数
            .build()
        val request3 = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(
                Data.Builder()
                    .putString("RequestType", "Third Request")
                    .build()
            ) //传入参数
            .build()
        //构建周期性请求，虽然写的是10秒周期，但是内部会将10秒改为15分钟，因为最小时间间隔是15分钟
        //val request = PeriodicWorkRequest.Builder(MyWorker::class.java, 10, TimeUnit.SECONDS).build()
        //////////////////////
        //当满足约束条件后才会执行该任务
        //WorkManager.getInstance(this).enqueue(request)
        WorkManager.getInstance(context)
            //使用beginWith()可以并行执行request、request1、request2
            .beginWith(arrayListOf(request, request2))
            //使用then()可以按顺序执行任务
            .then(request3)
            .enqueue()
        //////////////////////
        //取消请求
        //WorkManager.getInstance(this).cancelWorkById(request.id)
        //WorkManager.getInstance(this).cancelAllWorkByTag("tag") //根据请求标记清除所有关联请求
        LogUtil.i("Worker", "testWorkManager end")
    }

}