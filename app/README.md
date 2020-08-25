# app

主模块

## 目录结构说明

```
app
  |--src/main/java/com/example/learn //多级目录折叠
       |--algorithm //算法
       |--anim //实现点赞高光动画
       |--annotation //运行时注解，测试类：MainActivity
       |--constraint //ConstraintLayout与RelativeLayout性能分析
       |--download //测试download模块
       |--iconchange //动态切换应用ICON管理类
       |--jetpack //https://developer.android.com/jetpack
           |--room
           |--workmanager
                |--README.md //WorkManager源码浅析
       |--koin //koin
       |--lint //lint功能验证示例
       |--single //单例汇总
       |--ui //UI，暂不拆分为组件的类会放在这里，否则会放在UI组件库中
           |--appbarlayout //AppBarLayout使用示例
           |--bezier //贝塞尔曲线使用示例
           |--imagescaletype //ImageScaleType各类型区别
           |--viewswitcher //ViewSwitcher使用示例
       |--webview //WebView
  |--res
       |--anim //点赞高光动画相关资源
            |--raw //存放了动画的设计和规则
       |--appbarlayout //AppbarLayoutActivity相关资源
       |--imagescaletype //ImageScaleTypeActivity相关资源
       |--main //MainActivity相关资源
       |--viewswitcher //ViewSwitcherActivity相关资源
       |--webview //WebViewActivity相关资源
```