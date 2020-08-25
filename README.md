# AndroidLearn

学习总结Android： Java or Kotlin or Other

## 目录结构说明

```
learn
  |--annotation //编译时注解-注解定义和调用模块
       |--README.md //annotation模块说明
  |--app //主模块
       |--README.md //app模块说明
  |--common //公共业务模块
       |--README.md //common模块说明
  |--compiler //编译时注解-类动态生成模块
       |--README.md //编译时注解使用教程
  |--download //断点下载模块
       |--README.md //download模块说明
  |--git //git操作说明
  |--lintaar //别的模块依赖该模块，就能使用自定义的lint功能
       |--README.md //lintaar模块说明
  |--lintjar //实现自定义lint功能的模块
       |--README.md //lint教程
  |--plugin //Gradle插件模块，内部示例：APK文件名替换插件
       |--README.md //Gradle插件使用教程
  |--shake //摇一摇上传屏幕截图业务模块
       |--README.md //摇一摇上传屏幕截图业务模块说明
  |--ui //业务无关UI组件库
       |--README.md //ui模块说明
  |--utils //业务无关工具库
       |--README.md //utils模块说明
  |--webview_module //webview业务模块
       |--README.md //webview业务模块说明
```

## 未解决的问题

未解决的问题可以通过搜索`问题TODO`关键字搜索到

## FAQ

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