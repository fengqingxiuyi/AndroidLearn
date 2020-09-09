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