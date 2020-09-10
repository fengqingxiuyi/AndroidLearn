# gradleplugin

Gradle Plugin 独立模块实践，实现编译时修改APK名称的功能

- 官方教程：<https://docs.gradle.org/current/userguide/custom_plugins.html>
- 参考文章：<https://www.jianshu.com/p/3191c3955194>

# 开发步骤

## new Module

Module名随意。比如选择Java or Kotlin Library，这个修改起来简单点，选择其他的也一样需要修改

## 修改build.gradle

```groovy
apply plugin: 'groovy'
apply plugin: 'maven'

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation gradleApi()
}

//publish to local directory
def versionName = "1.0.0"
group "com.example.gradleplugin"
version versionName

uploadArchives{ //当前项目可以发布到本地文件夹中
    repositories {
        mavenDeployer {
            repository(url: uri('./repo')) //定义本地maven仓库的地址
        }
    }
}
```

## 修改项目文件夹

按照下面的目录结构修改即可

```
gradleplugin
   |-src
      |-main
         |-groovy
            |-自定义的包名，如com.example.gradleplugin //该文件夹中存放后缀名为groovy的文件
         |-resources
            |-META-INF
               |-gradle-plugins //该文件夹中存放后缀名为properties的文件
```

## 编写plugin

创建`ApkChangeNamePlugin.groovy`文件，并编写以下内容：

```groovy
package com.example.gradleplugin

import org.gradle.api.Project
import org.gradle.api.Plugin

/**
 * APK文件名替换插件
 */
class ApkChangeNamePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        if (!project.android) {
            throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!');
        }
        project.android.applicationVariants.all {
            variant ->
                variant.outputs.all {
                    outputFileName = "${variant.name}-${variant.versionName}.apk"
                }
        }
    }
}
```

## 编写properties

创建`apk-change-name.properties`文件，并编写以下内容：

```
# 指定实现类，com.example.gradleplugin为我自定义的包名
implementation-class=com.example.gradleplugin.ApkChangeNamePlugin
```

注意：文件名`apk-change-name`为apply plugin时会用到的plugin id，如：`apply plugin: 'apk-change-name'`。

## 发布

执行`./gradlew uploadArchives`命令，会将构建产物jar发布到build.gradle配置中指定的repo文件夹中，repo文件夹位置如下：


```
gradleplugin
   |-repo //该文件夹名称在build.gradle中配置，另外该文件夹会自动创建，无需手动创建
   |-src
```

## 使用

1. 在Project的build.gradle文件中增加如下配置：

```groovy
buildscript {
    //省略一些内容
    repositories {
        maven{
            url './gradleplugin/repo/' //这里写的是相对地址，也可以写绝对地址
        }
        //省略一些内容
    }
    dependencies {
        //省略一些内容
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath "com.example.gradleplugin:gradleplugin:1.0.0"
    }
    //省略一些内容
}
```

2. 在应用模块的build.gradle文件中使用

使用方式：`apply plugin: 'apk-change-name'`

需要注意的是，这个修改APK文件名的插件必须在`apply plugin: 'com.android.application'`或`apply plugin: 'com.android.library'`后面使用，因为在代码中限制了。

## 运行查看效果

打开`app/build/outputs/apk/debug/`文件夹查看效果是否发生变化。