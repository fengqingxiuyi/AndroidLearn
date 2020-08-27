package com.example.social.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.social.ISocialType;
import com.example.social.R;
import com.example.social.util.Utils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 微博分享帮助类
 * <p>
 * 相关文档
 * 1、移动客户端接入 http://open.weibo.com/wiki/%E7%A7%BB%E5%8A%A8%E5%AE%A2%E6%88%B7%E7%AB%AF%E6%8E%A5%E5%85%A5#SDK.E6.8E.A5.E5.85.A5.E6.B5.81.E7.A8.8B
 * 2、微博SDK 4.1文档.pdf https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E6%96%B0%E6%96%87%E6%A1%A3/%E5%BE%AE%E5%8D%9ASDK%204.1%E6%96%87%E6%A1%A3.pdf
 */
public class WBShareHelper {

    //分享文本
    public static final int TYPE_TEXT = 0;
    //分享图片（单张）
    public static final int TYPE_IMAGE_TEXT = 1;
    //分享图片（多张）
    public static final int TYPE_IMAGE_MULTI = 2;
    //分享视频
    public static final int TYPE_VIDEO = 3;
    //分享网页
    public static final int TYPE_WEB = 4;

    //上下文
    private Activity activity;
    //
    private WbShareHandler wbShareHandler;
    //分享结果回调
    private IShareCallback shareCallback;
    //图片缓存的父目录
    private File parentDir;
    //分享类型
    private int shareType;
    //是否需要finishActivity
    private boolean needFinishActivity;

    /**
     * 初始化微博
     */
    public WBShareHelper(Activity activity, String appId, String redirectUrl, File parentDir) {
        this.activity = activity;
        this.parentDir = parentDir;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            throw new RuntimeException("WeBo's appId or redirectUrl is empty!");
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, ""));
        wbShareHandler = new WbShareHandler(activity);
        wbShareHandler.registerApp();
    }

    /**
     * 具体的分享逻辑
     */
    public void share(ShareDataBean shareDataBean, IShareCallback shareCallback, Handler handler, boolean needFinishActivity) {
        this.shareCallback = shareCallback;
        this.needFinishActivity = needFinishActivity;
        //判断数据源是否为空
        if (shareDataBean == null) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wb_share_data);
            msg.arg1 = ISocialType.SOCIAL_WB;
            handler.sendMessage(msg);
            return;
        }
        //判断是否安装微博
        if (!WbSdk.isWbInstall(activity)) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wb_uninstall);
            msg.arg1 = ISocialType.SOCIAL_WB;
            handler.sendMessage(msg);
            return;
        }
        //获取分享类型
        if (shareDataBean.shareType == null) {
            shareType = 0;
        } else {
            shareType = shareDataBean.shareType.get(ISocialType.SOCIAL_WB);
        }
        //需要传递给微博的分享数据
        WeiboMultiMessage weiboMultiMessage = getShareMessage(shareDataBean);
        if (weiboMultiMessage == null) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wb_share_data);
            msg.arg1 = ISocialType.SOCIAL_WB;
            handler.sendMessage(msg);
            return;
        }
        //分享到微博
        wbShareHandler.shareMessage(weiboMultiMessage, false);
    }

    /**
     * 需要传递给微博的分享数据
     */
    private WeiboMultiMessage getShareMessage(ShareDataBean shareDataBean) {
        WeiboMultiMessage msg = new WeiboMultiMessage();
        BaseMediaObject mediaObject = null;
        switch (shareType) {
            case WBShareHelper.TYPE_TEXT:
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                mediaObject = msg.textObject;
                break;
            case WBShareHelper.TYPE_IMAGE_TEXT:
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                msg.imageObject = getImageObj(shareDataBean.shareImage);
                mediaObject = msg.imageObject;
                break;
            case WBShareHelper.TYPE_IMAGE_MULTI:
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                msg.multiImageObject = getMultiImgObj(shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage, shareDataBean.shareImageList);
                mediaObject = msg.multiImageObject;
                break;
            case WBShareHelper.TYPE_VIDEO:
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                msg.videoSourceObject = getVideoObj(shareDataBean.shareImage, shareDataBean.shareVideoUrl);
                mediaObject = msg.videoSourceObject;
                break;
            case WBShareHelper.TYPE_WEB:
                msg.textObject = getTextObj(shareDataBean.shareDesc);
                msg.mediaObject = getWebPageObj(shareDataBean.shareUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                mediaObject = msg.mediaObject;
                break;
        }
        if (mediaObject == null) {
            return null;
        }
        return msg;
    }

    private TextObject getTextObj(String desc) {
        TextObject textObject = new TextObject();
        textObject.text = desc;
        return textObject;
    }

    private ImageObject getImageObj(String image) {
        String imagePath = ImageUtil.getLocalImagePath(parentDir, image);
        if (TextUtils.isEmpty(imagePath) || imagePath.length() > 512) {
            return null;
        }
        File file = new File(imagePath);
        if (!file.exists() || file.length() == 0L || file.length() > 10485760L) {
            return null;
        }
        ImageObject imageObject = new ImageObject();
        imageObject.imagePath = imagePath;
        return imageObject;
    }

    /**
     * @param imageList 本地图片集合
     */
    private MultiImageObject getMultiImgObj(String title, String desc, String image, List<String> imageList) {
        MultiImageObject multiImageObject = new MultiImageObject();
        ArrayList<Uri> uris = new ArrayList<>();
        if (imageList != null) {
            for (String imageStr : imageList) {
                if (TextUtils.isEmpty(imageStr)) {
                    continue;
                }
                uris.add(Uri.fromFile(new File(imageStr)));
            }
        }
        multiImageObject.setImageList(uris);
        if (addTitleSummaryAndThumb(multiImageObject, title, desc, image)) {
            return null;
        }
        return multiImageObject;
    }

    /**
     * @param image 本地图片
     */
    private VideoSourceObject getVideoObj(String videoUrl, String image) {
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        if (!TextUtils.isEmpty(videoUrl)) {
            videoSourceObject.videoPath = Uri.fromFile(new File(videoUrl));
        }
        if (!TextUtils.isEmpty(image)) {
            videoSourceObject.coverPath = Uri.fromFile(new File(image));
        }
        return videoSourceObject;
    }

    private WebpageObject getWebPageObj(String url, String title, String desc, String image) {
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = Utility.generateGUID();
        webpageObject.actionUrl = url;
        if (addTitleSummaryAndThumb(webpageObject, title, desc, image)) {
            return null;
        }
        return webpageObject;
    }

    /**
     * 当有设置缩略图但是找不到的时候阻止分享
     */
    private boolean addTitleSummaryAndThumb(BaseMediaObject msg, String title, String desc, String image) {
        msg.title = title;
        msg.description = desc;
        byte[] imageData = ImageUtil.getImageByte(image, 32768);
        if (imageData == null) {
            return true;
        }
        msg.thumbData = imageData;
        return false;
    }

    /**
     * 微博开放平台需要
     */
    public void onNewIntent(Intent intent) {
        if (wbShareHandler != null) {
            wbShareHandler.doResultIntent(intent, wbShareCallback);
        }
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }
    }

    /**
     * 微博的分享监听器
     */
    private WbShareCallback wbShareCallback = new WbShareCallback() {
        @Override
        public void onWbShareSuccess() {
            if (shareCallback != null) {
                shareCallback.onSuccess(ISocialType.SOCIAL_WB, null);
            }
            Utils.finish(activity, needFinishActivity);
        }

        @Override
        public void onWbShareCancel() {
            if (shareCallback != null) {
                shareCallback.onCancel(ISocialType.SOCIAL_WB);
            }
            Utils.finish(activity, needFinishActivity);
        }

        @Override
        public void onWbShareFail() {
            if (shareCallback != null) {
                shareCallback.onError(ISocialType.SOCIAL_WB, null);
            }
            Utils.finish(activity, needFinishActivity);
        }
    };

}
