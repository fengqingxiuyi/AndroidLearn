# image

图片组件

参考自：<https://github.com/ShowJoy-com/SHImageView>
文档：<https://www.fresco-cn.org/>

# 简单使用

首先需要在Application类中的初始化，如下：

```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FImageView.init(this);
    }

}
```

然后先看下在Xml中的使用示例：

```xml
<com.example.image.FImageView
    android:id="@+id/image_view"
    android:layout_width="50dp"
    android:layout_height="50dp"
    android:layout_margin="10dp"
    fresco:placeholderImage="@mipmap/ic_launcher" />
```

```java
FImageView imageView = (FImageView) findViewById(R.id.image_view);
imageView.setImageUrl(R.mipmap.image_png);
```

再看下通过Java代码实现的示例：

```java
FImageView imageView = new FImageView(context);
imageView.setImageUrl(R.mipmap.image_png);
```

# 详细使用

见`com.example.learn.ui.image.ImageActivity.java`