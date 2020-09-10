# utils

业务无关工具库

## 目录结构说明

```
utils
  |--src/main/java/com/example/utils //多级目录折叠
       |--activity //activity工具模块
            |--ActivitiesManager //Activity栈统一管理类
            |--ActivityUtil //获取Activity信息工具类
       |--anim //生成动画工具模块
       |--app //app工具模块
            |--AppLifecycleMonitor //App生命周期监听类
            |--AppUtil //获取应用信息的工具类
            |--ChannelUtil //获取应用渠道信息的工具类
       |--device //和设备相关的工具模块
            |--CameraUtil //处理相机的工具类
            |--ClipboardUtil //复制粘贴文本的工具类
            |--DensityUtil //尺寸大小转换工具类
            |--DeviceUtil //获取设备信息的工具类
            |--GalleryUtil //保存图片到相册的两种方式
            |--InputMethodUtil //输入法的显示与隐藏
            |--IntentUtil //集成一些常用的系统相关的Intent
            |--StatusBarUtil //控制状态栏的工具类
       |--download //下载工具模块
            |--DownloadUtil //下载工具类
       |--encrypt //加密算法工具模块
            |--AesUtils //实现AES加密解密的工具类
            |--Base64Util //实现Base64加密解密的工具类
            |--MD5Util //实现MD5加密解密的工具类
       |--event //全局事件工具模块
            |--RxBusUtil //通过RxJava实现的事件总线
       |--format //数字格式化模块
            |--CalculateAgeUtil //计算年龄工具类
            |--NumFormat //数字格式化工具类
            |--RegexUtil //正则表达式工具类
       |--iconchange //动态切换应用ICON模块
            |--IconChangeUtil //实现应用ICON切换的工具类
            |--README.md //动态切换应用ICON的设计、使用与局限总结
       |--network //网络工具模块
            |--NetworkUtil //网络工具类
            |--UrlUtil //Url编解码工具类
       |--permission //运行时申请权限模块
            |--CheckPermissionActivity //基础权限Activity
            |--PermissionUtil //运行时申请权限工具类
       |--storage //存储模块
            |--FileUtil //文件处理工具类
            |--SharedPreferencesUtil //SharedPreferences存储工具类
            |--StorageManagerUtil //存储管理工具类，先存储在缓存再存储在SharedPreferences中
       |--view //View相关的工具模块
            |--EditTextUtil //EditText工具处理类
            |--ImageUtil //Bitmap工具处理类
            |--ShapeUtil //Shape工具处理类
            |--TextViewUtil //TextView工具处理类
       |--LogUtil //打印日志工具类
```