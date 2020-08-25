# webview_module

webview业务模块

## 目录结构说明

```
webview_module
  |--src/main/
       |--aidl/com/example/webview_module //多级目录折叠
            |--IWebviewBinder.aidl //主进程
            |--IWebviewBinderCallback.aidl //子进程
       |--java/com/example/webview_module //多级目录折叠
            |--binder //binder服务模块
            |--constants //常量模块
            |--cookie //cookie管理
            |--js //JS交互注册模块
            |--router //路由中转
            |--service //Service
            |--setting //配置模块
            |--utils //工具模块
            |--widget //页面组件
            |--WebviewActivity //Webview页面
```

## 使用

1. 在Application中初始化

```kotlin
if (AppUtil.isMainProcess(this)) {
    JsBridge.get().register(JsInterfaceImpl::class.java)
}
```

2. 在app模块中创建JsInterfaceImpl类，参考switchApi函数的写法获取前端传递过来的参数，执行对应的业务逻辑，内容如下：

```java
public class JsInterfaceImpl {

    public void switchApi(Context context, String params, IWebviewBinderCallback callback) {
        if (checkContextAndParamsError(context, params)) {
            return;
        }
        //拿到params之后执行切换API的业务逻辑
    }

}
```