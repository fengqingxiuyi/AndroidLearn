# app

主模块

## 目录结构说明

```
app
  |--src/debug/java/com/example/learn //多级目录折叠-debug包专属功能
       |--fps //FPS功能模块
       |--AppBlockCanaryContext //BlockCanary配置文件
       |--Config //配置文件
  |--src/main //多级目录折叠
       |--assets //资源文件目录，不会处理内部资源，通过文件路径+文件名引用
            |--fonts //存放字体文件，暂用于测试iconfont功能
            |--long_image.jpg //测试长图加载显示功能
            |--test.html //测试Native与Webview的交互功能
       |--cpp //cpp文件目录
            |--CMakeLists.txt //cpp配置文件
            |--native-lib.cpp //第一个JNI程序，cpp端的实现
            |--README.md //第一个JNI程序实现总结
       |--java/com/example/learn //多级目录折叠
           |--algorithm //算法
           |--annotation //运行时注解，测试类：MainActivity
           |--iconchange //动态切换应用ICON管理类
           |--java //Java功能测试模块
           |--jetpack //https://developer.android.com/jetpack
               |--room
               |--viewmodel //MVVM示例
               |--workmanager
                    |--README.md //WorkManager源码浅析
           |--koin //koin
           |--lint //lint功能验证示例
           |--single //单例汇总
           |--ui
               |--adapter //CommonAdapter使用示例
               |--anim //实现点赞高光动画
               |--appbarlayout //AppBarLayout使用示例
               |--banner //轮播组件使用示例
               |--bezier //贝塞尔曲线使用示例
               |--card //卡片形式的View使用示例
               |--constraint //ConstraintLayout与RelativeLayout性能分析
               |--download //测试download模块
               |--image //图片组件使用示例
               |--imagescaletype //ImageScaleType各类型区别
               |--layout2bitmap //Layout 转换为 Bitmap
               |--loop //单个或两个条目无限上下翻滚的View容器使用示例
               |--network //网络请求库测试
               |--partition //分区组件使用示例
               |--player //视频播放器使用示例
               |--refresh //刷新加载组件使用示例
               |--social //社会化组件测试
               |--viewattribute //自定义View并实现属性的设置与使用
               |--viewswitcher //ViewSwitcher使用示例
               |--webview //WebView
                    |--README.md //Java与JS的交互总结
               |--youtu //腾讯优图
                    |--README.md //腾讯优图使用总结
           |--uiutils //将AndroidUtils库中的ui和utils类迁移过来，此文件夹使测试示例
           |--wxapi //微信授权和分享需要的文件夹
       |--res //资源文件目录，会将资源生成对应的R文件，通过id引用
       |--AndroidManifest.xml //app模块配置清单文件
  |--src/release/java/com/example/learn //多级目录折叠-release包专属功能
       |--Config //配置文件
```