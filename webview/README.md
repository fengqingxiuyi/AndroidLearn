# webview

webview模块

## 目录结构说明

```
webview
  |--src/main/
       |--aidl/com/example/webview //多级目录折叠
            |--IWebviewBinder.aidl //主进程
            |--IWebviewBinderCallback.aidl //子进程
       |--java/com/example/webview //多级目录折叠
            |--binder //binder服务模块
            |--business //业务模块
                 |--cookie //cookie管理
                 |--router //路由中转
            |--constants //常量模块
            |--js //JS交互注册模块
            |--service //Service
            |--setting //配置模块
            |--utils //工具模块
            |--view //WebviewActivity
            |--widget //页面组件
```