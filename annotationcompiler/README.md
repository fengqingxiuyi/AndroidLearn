# annotationcompiler

编译时注解

参考文章：<https://juejin.im/post/6844903732912586765>

# 开发步骤

## 创建注解模块

new Module，选择Java or Kotlin Library。命名为annotation，用于存放注解。

build.gradle内容如下：

```groovy
apply plugin: 'java-library'
apply plugin: 'kotlin'

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
}
```

创建注解BindCompile，其内容如下：

```kotlin
package com.example.annotation

/**
 * @author fqxyi
 * @date 2020/8/14
 * 编译时注解，注解属性
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class BindCompile(val value: Int)
```

## 创建编译模块

new Module，选择Java or Kotlin Library。命名为annotationcompiler，用于存放Processor注解处理类。另外该模块需要依赖前面的注解模块。

build.gradle内容如下：

```groovy
apply plugin: 'java-library'
apply plugin: 'kotlin'
apply plugin: 'kotlin-kapt'

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    //https://github.com/google/auto/tree/master/service 注解 processor 类，并对其生成 META-INF 的配置信息
    implementation 'com.google.auto.service:auto-service:1.0-rc7'
    kapt 'com.google.auto.service:auto-service:1.0-rc7'
    //https://github.com/square/kotlinpoet
    implementation 'com.squareup:kotlinpoet:1.6.0'
    //
    implementation project(":annotation")
}
```

创建注解处理类CompileProcessor，其内容如下：

```kotlin
package com.example.annotationcompiler
 
 import com.example.annotation.BindCompile
 import com.google.auto.service.AutoService
 import com.squareup.kotlinpoet.ClassName
 import com.squareup.kotlinpoet.FileSpec
 import com.squareup.kotlinpoet.FunSpec
 import com.squareup.kotlinpoet.TypeSpec
 import java.util.*
 import javax.annotation.processing.*
 import javax.lang.model.element.TypeElement
 import javax.lang.model.util.Elements
 import kotlin.collections.HashMap
 import kotlin.collections.HashSet
 
 
 /**
  * @author fqxyi
  * @date 2020/8/14
  */
 @AutoService(Processor::class)
 class CompileProcessor : AbstractProcessor() {
 
     /**
      * 返回当前处理器能处理的注解集合
      */
     override fun getSupportedAnnotationTypes(): MutableSet<String> {
         return Collections.singleton(BindCompile::class.java.canonicalName)
     }
 
     private var elementUtils: Elements? = null
 
     //自定义类要存放的文件位置
     private var filer: Filer? = null
 
     override fun init(processingEnv: ProcessingEnvironment?) {
         super.init(processingEnv)
         elementUtils = processingEnv?.elementUtils
         filer = processingEnv?.filer
     }
 
     //存储含有BindCompile注解的类的集合
     private var typeElementSet = HashSet<TypeElement>()
 
     override fun process(
         annotations: MutableSet<out TypeElement>?,
         roundEnv: RoundEnvironment?
     ): Boolean {
         val elements = roundEnv?.getElementsAnnotatedWith(BindCompile::class.java)
         //获取含有BindCompile注解的类的集合
         typeElementSet.clear()
         elements?.forEach { member ->
             val typeElement = member.enclosingElement as TypeElement
             typeElementSet.add(typeElement)
         }
         typeElementSet.iterator().forEach { typeElement ->
             //创建一个bindView的方法，参数为activity，并使用JvmStatic注解
             val bindFunBuilder = FunSpec.builder("bindView")
                 .addParameter("activity", ClassName.bestGuess(typeElement.toString()))
                 .addAnnotation(JvmStatic::class.java)
             //获取所有@BindCompile注解的属性
             elements?.forEach { member ->
                 val bindCompile: BindCompile? = member.getAnnotation(BindCompile::class.java)
                 //此处需要typeElement == member.enclosingElement判断，因为elements里包含了所有类中定义了BindCompile的注解
                 if (bindCompile != null && typeElement == member.enclosingElement) {
                     //方法中添加findViewById
                     bindFunBuilder.addStatement("activity.${member.simpleName} = activity.findViewById(${bindCompile.value})")
                 }
             }
             //生成一个由@BindCompileClass注解的类的名称加_bindView后缀的类，其中有一个静态方法bindView
             val fileSpec = FileSpec.builder(
                 getPackageName(typeElement),
                 typeElement.simpleName.toString() + "_bindView"
             )
                 .addComment("Generated code from Butter Knife. Do not modify!")
                 .addType(
                     TypeSpec.classBuilder(typeElement.simpleName.toString() + "_bindView")
                         .addType(
                             TypeSpec.companionObjectBuilder()
                                 .addFunction(bindFunBuilder.build())
                                 .build()
                         )
                         .build()
                 )
                 .build()
             filer?.apply {
                 fileSpec.writeTo(this)
             }
         }
         return true
     }
 
     private fun getPackageName(type: TypeElement): String {
         return elementUtils?.getPackageOf(type)?.qualifiedName.toString()
     }
 
 }
```

## 应用

### 在要应用的模块的build.gradle中添加依赖：

```groovy
apply plugin: 'kotlin-kapt'
//省略一些内容
dependencies {
    //省略一些内容
    //编译时注解
    implementation project(":annotation")
    kapt project(":annotationcompiler")
}
```

### 通过反射方式调用编译时创建的bindView函数

```kotlin
package com.example.annotation

import android.util.Log

/**
 * @author fqxyi
 * @date 2020/8/14
 */
object Binding {
    /**
     * 编译时注解
     */
    fun bindCompile(target: Any) {
        try {
            val classs = target.javaClass
            val claName = classs.name + "_bindView"
            val clazz = Class.forName(claName)

            val bindMethod = clazz.getMethod("bindView", target::class.java)
            val ob = clazz.newInstance()
            bindMethod.invoke(ob, target)
        } catch (e : Exception) {
            println("Binding Compile => " +  e.message)
        }
    }
}
```

### 使用

```kotlin
//编译时注解 定义
@BindCompile(R.id.bindCompileText) var bindCompileText: TextView? = null

//编译时注解 使用
Binding.bindCompile(this)
bindCompileText?.text = "Binding Compile Success"
```

## 测试类

- MainActivity：测试多个id的情况
- ConstraintActivity：测试在另外一个类中的情况