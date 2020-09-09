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
