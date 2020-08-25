# utils

业务无关工具库

## 目录结构说明

```
utils
  |--src/main/java/com/example/utils //多级目录折叠
       |--anim //生成动画工具模块
       |--device //和设备相关的工具模块
            |--ClipboardUtil //复制粘贴文本的工具类
            |--DensityUtil //尺寸大小转换工具类
            |--DeviceUtil //获取设备信息的工具类
            |--StatusBarUtil //控制状态栏的工具类
       |--encrypt //加密算法工具模块
            |--AesUtils //实现AES加密解密的工具类
            |--Base64Util //实现Base64加密解密的工具类
       |--iconchange //动态切换应用ICON模块
            |--IconChangeUtil //实现应用ICON切换的工具类
            |--README.md //动态切换应用ICON的设计、使用与局限总结
       |--permission //运行时申请权限模块
       |--ActivitiesManager //Activity栈统一管理类
       |--AppLifecycleMonitor //App生命周期监听类
       |--AppUtil //获取应用信息的工具类
       |--LogUtil //打印日志工具类
```