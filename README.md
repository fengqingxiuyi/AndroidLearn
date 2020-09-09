# AndroidLearn

学习总结Android： Java or Kotlin or Other

## 目录结构说明

```
learn
  |--annotation //编译时注解-注解定义和调用模块
       |--README.md //annotation模块说明
  |--annotationcompiler //编译时注解-类动态生成模块
       |--README.md //编译时注解使用教程
  |--app //主模块
       |--README.md //app模块说明
  |--git //git操作总结
       |--README.md //git操作总结
  |--gradleplugin //Gradle插件模块，内部示例：APK文件名替换插件
       |--README.md //Gradle插件使用教程
  |--library //存放基础模块的文件夹
       |--aop //AOP组件
            |--README.md //AOP组件说明
       |--banner //轮播组件
            |--README.md //轮播组件说明
       |--download //断点下载模块
            |--README.md //download模块说明
       |--image //图片组件
            |--README.md //图片组件说明
       |--network //网络请求模块
            |--README.md //网络请求模块说明
       |--partition //实现分区功能的模块
            |--README.md //分区组件说明
       |--refresh //刷新加载组件
            |--README.md //刷新加载组件说明
       |--ui //业务无关UI组件库
            |--README.md //ui模块说明
       |--utils //业务无关工具库
            |--README.md //utils模块说明
  |--lintaar //别的模块依赖该模块，就能使用自定义的lint功能
       |--README.md //lintaar模块说明
  |--lintjar //实现自定义lint功能的模块
       |--README.md //lint教程
  |--module //存放业务模块的文件夹
       |--common //公共业务模块
            |--README.md //common模块说明
       |--shake //摇一摇上传屏幕截图业务模块
            |--README.md //摇一摇上传屏幕截图业务模块说明
       |--webview_module //webview业务模块
            |--README.md //webview业务模块说明
```

## 未解决的问题

未解决的问题可以通过搜索`问题TODO`关键字搜索到

## FAQ

### Fragment中findViewById使用注意点

```kotlin
package com.example.learn.ui.appbarlayout

class AppbarLayoutFragment : Fragment() {

//    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.appbar_layout_fragment, container, false)
        /**
         * 如果init函数在return之前执行，那么下面这句findViewById不能省略，否则会抛出java.lang.IllegalStateException: Fragment already added异常
         */
//        recycler = view.findViewById<View>(R.id.recycler) as RecyclerView
//        init()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * init函数正确使用位置应该在这里
         */
        init()
    }

    private fun init() {
        recycler.adapter
    }
}
```

### Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6

使用`OneTimeWorkRequestBuilder`类时报以上错误

```groovy
android {
    //省略一些内容
    //FIX Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8
    }
}
```

### 运行Java文件的main函数报错

报错信息如下所示：

```
FAILURE: Build failed with an exception.

* Where:
Initialization script '/private/var/folders/2m/k9j0518x7s99vygl8br1pc0h0000gn/T/SingleTest_main__.gradle' line: 21

* What went wrong:
A problem occurred configuring project ':app'.
> Could not create task ':app:SingleTest.main()'.
   > SourceSet with name 'main' not found.
```

编辑`.idea/gradle.xml`文件，在`GradleProjectSettings`节点下新增option`<option name="delegatedBuild" value="false" />`，详情如下所示：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="GradleMigrationSettings" migrationVersion="1" />
  <component name="GradleSettings">
    <option name="linkedExternalProjectsSettings">
      <GradleProjectSettings>
        <!-- 新增以下option -->
        <option name="delegatedBuild" value="false" />
        <!-- 省略其他option -->
      </GradleProjectSettings>
    </option>
  </component>
</project>
```