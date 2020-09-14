# ExoPlayer

# 参考文章

- [ExoPlayer](https://github.com/google/ExoPlayer)

# FAQ

## Process 'command 'C:\software\sdk\build-tools\27.0.3\aidl.exe'' finished with non-zero exit value 1

这是由于我在aidl中引用了非基本类型类导致，解决办法就是为新类制作一个aidl，内容如下：

```aidl
// IFPlayer.aidl
package com.fqxyi.player; //IFPlayer.aidl的包名全路径

// Declare any non-default types here with import statements
import com.fqxyi.player.IFPlayer; //IFPlayer.java的包名全路径

parcelable IFPlayer;
```
