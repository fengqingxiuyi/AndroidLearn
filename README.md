# Learn

学习 Android or Java or Kotlin or Other

## 目录结构说明

```
learn
  |--annotation //编译时注解-注解定义和调用模块
  |--app
      |--src/main/java/com/example/learn //多级目录折叠
           |--algorithm //算法
           |--annotation //运行时注解
           |--constraint //ConstraintLayout与RelativeLayout性能分析
           |--jetpack
                |--room //room
           |--koin //koin
           |--single //单例汇总
  |--compiler //编译时注解-类动态生成模块
       |--README.md //编译时注解使用教程
  |--plugin //Gradle插件模块，内部示例：APK文件名替换插件
       |--README.md //Gradle插件使用教程
```

## FAQ

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