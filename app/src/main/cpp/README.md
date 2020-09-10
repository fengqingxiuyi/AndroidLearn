# cpp

- [add-native-code](https://developer.android.com/studio/projects/add-native-code?hl=zh-cn)
- [ndk-samples](https://github.com/android/ndk-samples)

# 第一个JNI程序

1. 创建存放`cpp`的目录，如：app/src/main/cpp

2. 在`com.example.learn.TestActivity`类中添加以下代码：

```kotlin
package com.example.learn

class TestActivity : BaseActivity() {
    /**
     * external：Kotlin里标识一个方法是JNI方法的关键字
     */
    external fun stringFromJNI(): String?
    
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
    
    fun testJNI(view: View) {
        ToastUtil.toast(stringFromJNI())
    }
}
```

3. 创建`native-lib.cpp`文件，作为第一个CPP程序，代码如下：

**注意：** 函数名的格式为`Java_(包名并且用_符号替换.符号)_(类名)_(类中定义的函数名)`，例如：`Java_com_example_learn_TestActivity_stringFromJNI`。

```objectivec
//
// Created by 风清袖一 on 2020/9/1.
//

#include <jni.h>
#include <string>
#include <android/log.h>

//定义输出的TAG
const char * TAG = "NATIVE-LIB";

/**
 * 告诉编译器在编译这个函数名时按着C的规则去翻译相应的函数名而不是C++的，
 * C++的规则在翻译这个函数名时会把这个名字变得面目全非，不同的编译器采用的方法不一样，
 * 原因就是C++支持函数的重载。
 */
extern "C"
/**
 * Windows 中如果需要生成动态库 , 并且需要将该动态库交给其它项目使用 , 需要在方法前加入特殊标识 , 才能 在外部 程序代码中 调用该 DLL 动态库中定义的方法 ;
 * ① Windows 平台 : 需要将方法 返回值 之前加入 __declspec(dllexport) 标识 ;
 * ② Linux 平台 : 需要将方法 返回值 之前加入 attribute ((visibility (“default”))) 标识 ;
 * 该声明的作用是保证在本动态库中声明的方法 , 能够在其他项目中可以被调用 ;
 */
JNIEXPORT jstring
/**
 * 作用：
 * ① Windows JNICALL : JNICALL 被定义为 __stdcall , __stdcall 是一种函数调用参数的约定 ,
 * 在 Windows 中调用函数时 , 该函数的参数是以 栈 的形式保存的 , 栈 中元素是后进先出的 , __stdcall 表示参数是从右到左保存的 ;
 * __stdcall 用于 定义 函数入栈规则 ( 从右到左 ) , 和 堆栈清理规则 ;
 * ② Linux JNICALL : JNICALL 没有进行定义 , 直接置空 ; 在 Linux 中可以不用写 JNIEXPORT 和 JNICALL 宏 ;
 */
JNICALL
/**
 * 第一个JNI程序
 * @param env 指向全部JNI方法的指针。该指针只在创建它的线程有效，不能跨线程传递。
 * @param thiz 指代了其所指代的类,进而访问成员方法和成员变量等。
 * @return
 */
Java_com_example_learn_TestActivity_stringFromJNI(JNIEnv *env, jobject thiz)
{
    #if defined(__arm__)
        #if defined(__ARM_ARCH_7A__)
            #if defined(__ARM_NEON__)
                #if defined(__ARM_PCS_VFP)
                    #define ABI "armeabi-v7a/NEON (hard-float)"
                #else
                    #define ABI "armeabi-v7a/NEON"
                #endif
            #else
                #if defined(__ARM_PCS_VFP)
                    #define ABI "armeabi-v7a (hard-float)"
                #else
                    #define ABI "armeabi-v7a"
                #endif
            #endif
        #else
            #define ABI "armeabi"
        #endif
    #elif defined(__i386__)
        #define ABI "x86"
    #elif defined(__x86_64__)
        #define ABI "x86_64"
    #elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
        #define ABI "mips64"
    #elif defined(__mips__)
        #define ABI "mips"
    #elif defined(__aarch64__)
        #define ABI "arm64-v8a"
    #else
        #define ABI "unknown"
    #endif

    std::string hello = "Hello from JNI !  Compiled with ABI " ABI ".";

    //输出Verbose级别的日志信息
    __android_log_print(ANDROID_LOG_VERBOSE, TAG, "hello native log");

    return env->NewStringUTF(hello.c_str());
}
```

4. 创建`CMakeLists.txt`文件，配置CPP信息和依赖，有点类似于Gradle的作用：

```cmake
# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

# 限定cmake支持最低版本
cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

# 配置so库的信息
add_library( # Specifies the name of the library.
             # 生成的so库名称,并不需要和c/cpp文件名相同
             # 这里生产的so库名称将为libnative-lib.so
             native-lib

             # Sets the library as a shared library.
             # STATIC：静态库，是目标文件的归档文件，在链接其它目标的时候使用
             # SHARED：动态库，会被动态链接，在运行时被加载
             # MODULE：模块库，是不会被链接到其它目标中的插件，但是可能会在运行时使用dlopen-系列的函数动态链接
             SHARED

             # Provides a relative path to your source file(s).
             # 资源文件的路径，可以是多个资源文件
            native-lib.cpp)

# 从系统库中查找依赖库
find_library( # Defines the name of the path variable that stores the
              # location of the NDK library.
              # 设置依赖库的名字，下面链接库的时候会用到
              log-lib

              # Specifies the name of the NDK library that
              # CMake needs to locate.
              # 查找log依赖库
              # {sdk-path}/ndk-bundle/sysroot/usr/include/android/log.h
              log )

# Links your native library against one or more other native libraries.
# 配置库的依赖关系(链接关系)
target_link_libraries( # Specifies the target library.
                       # 目标库
                       native-lib

                       # Links the log library to the target library.
                       # 依赖库，可以是多个
                       ${log-lib} )
```

5. 配置要使用jni功能的模块的`build.gradle`文件，配置以下数据：

```groovy
android {
    //省略一些内容
    //指定NDK版本，可选项
    ndkVersion '21.3.6528147'
    // This block is different from the one you use to link Gradle
    // to your CMake or ndk-build script.
    externalNativeBuild {
        // Encapsulates your CMake build configurations.
        // cmake选项：https://google.github.io/android-gradle-dsl/current/com.android.build.gradle.internal.dsl.ExternalNativeCmakeOptions.html
        cmake {
            // Provides a relative path to your CMake build script.
            path "src/main/cpp/CMakeLists.txt"
        }
    }
    //省略一些内容
}
```

6. 运行测试