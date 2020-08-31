# social

社会化组件

open_sdk_r6008_lite.jar：<http://wiki.open.qq.com/wiki/QQ%E7%99%BB%E5%BD%95%E5%92%8C%E6%B3%A8%E9%94%80>

# 集成步骤

1、依赖library库

2、给Project的build.gradle文件添加如下代码：

```
allprojects {
    repositories {
        //省略其余配置
        maven { url "https://dl.bintray.com/thelasterstar/maven/" }
    }
}
```

3、修改social组件中AndroidManifest.xml的配置：

主要是将WXEntryActivity和WXPayEntryActivity的包名修改，以及修改AuthActivity的scheme。如下：

```xml
<application>
    <!--微信配置开始-->
    <activity
        android:name="com.example.learn.wxapi.WXEntryActivity"
        android:exported="true" />
    <activity
        android:name="com.example.learn.wxapi.WXPayEntryActivity"
        android:exported="true" />
    <!--微信配置结束-->
    <!--qq配置开始-->
    <activity
        android:name="com.tencent.tauth.AuthActivity"
        android:launchMode="singleTask"
        android:noHistory="true">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />

            <data android:scheme="tencent1107009250" />
        </intent-filter>
    </activity>
    <activity
        android:name="com.tencent.connect.common.AssistActivity"
        android:configChanges="orientation|keyboardHidden|screenSize"
        android:theme="@android:style/Theme.Translucent.NoTitleBar" />
    <!--qq配置结束-->
</application>
```

4、在自己的包名下添加wxapi包及里面的所有java文件

5、在自定义的Application中初始化

```java
//初始化数据
SocialHelper.get().setQqAppId("1107009250")
        .setWxAppId("wx2847b18acb41e535")
        .setWxAppSecret("78f713b76c61a38242e63ccdb3a96d68")
        .setWbAppId("2214687859")
        .setWbRedirectUrl("https://github.com/fengqingxiuyi");
```

# 功能详情

各功能使用详情，请参考`com.example.learn.ui.social.SocialActivity`

例如：

```java
/**
 * 分享
 */
public void jump2Share(View view) {
    ShareDataBean shareDataBean = new ShareDataBean();
    HashMap<Integer, Integer> shareTypeList = new HashMap<>();
    shareTypeList.put(ISocialType.SOCIAL_WX_SESSION, WXShareHelper.TYPE_WEB);
    shareTypeList.put(ISocialType.SOCIAL_WX_TIMELINE, WXShareHelper.TYPE_WEB);
    shareTypeList.put(ISocialType.SOCIAL_WX_MINIPROGRAM, WXShareHelper.TYPE_MINIPROGRAM);
    shareTypeList.put(ISocialType.SOCIAL_QQ, QQShareHelper.TYPE_IMAGE_TEXT);
    shareTypeList.put(ISocialType.SOCIAL_WB, WBShareHelper.TYPE_TEXT);
    shareDataBean.shareType = shareTypeList;
    shareDataBean.shareTitle = "百度一下，你就知道";
    shareDataBean.shareDesc = "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。";
    shareDataBean.shareImage = "https://www.baidu.com/img/bd_logo1.png";
    shareDataBean.shareUrl = "https://www.baidu.com/";
    shareDataBean.shareMiniType = 0; //小程序类型 - 正式版:0，测试版:1，体验版:2
    shareDataBean.shareMiniAppId = "gh_64c734bc4b8d"; //小程序AppId
    shareDataBean.shareMiniPage = "pages/fitting-room/index"; //小程序页面地址

    ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_TIMELINE));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SMS));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COPY));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_REFRESH));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_CUSTOM, "https://img.ezprice.com.tw/is/c.rimg.com.tw/s1/4/7e/29/21628111029801_843_s.jpg", "自定义图标需要集成图片库"));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WB));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_MINIPROGRAM));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_ALIPAY));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COLLECTION));
    socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SHOW_ALL));

    SocialHelper.get().share(this, socialTypeBeans, shareDataBean, new IShareCallback() {
        @Override
        public void onSuccess(int socialType, String msg) {
            Toast.makeText(MainActivity.this, "MainActivity onSuccess, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(int socialType, String msg) {
            Toast.makeText(MainActivity.this, "MainActivity onError, socialType = " + socialType +", msg = " + msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(int socialType) {
            Toast.makeText(MainActivity.this, "MainActivity onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
        }
    });
}
```
