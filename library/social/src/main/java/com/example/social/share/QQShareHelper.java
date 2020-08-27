package com.example.social.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.social.ISocialType;
import com.example.social.R;
import com.example.social.util.Utils;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * QQ分享帮助类
 * <p>
 * 相关文档：
 * 1、分享消息到QQ（定向分享） http://wiki.open.qq.com/wiki/%E5%88%86%E4%BA%AB%E6%B6%88%E6%81%AF%E5%88%B0QQ%EF%BC%88%E5%AE%9A%E5%90%91%E5%88%86%E4%BA%AB%EF%BC%89
 */
public class QQShareHelper {

    //分享图文
    public static final int TYPE_IMAGE_TEXT = 0;
    //分享本地图片
    public static final int TYPE_IMAGE = 1;
    //分享音乐
    public static final int TYPE_MUSIC = 2;
    //分享应用
    public static final int TYPE_APP = 3;

    //上下文
    private Activity activity;
    //
    private Tencent tencent;
    //分享结果回调
    private IShareCallback shareCallback;
    //图片缓存的父目录
    private File parentDir;
    //分享类型
    private int shareType;
    //是否需要finishActivity
    private boolean needFinishActivity;

    /**
     * 初始化QQ
     */
    public QQShareHelper(Activity activity, String appId, File parentDir) {
        this.activity = activity;
        this.parentDir = parentDir;
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("QQ's appId is empty!");
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
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
            msg.obj = activity.getString(R.string.social_error_qq_share_data);
            msg.arg1 = ISocialType.SOCIAL_QQ;
            handler.sendMessage(msg);
            return;
        }
        //判断是否安装QQ
        if (!tencent.isQQInstalled(activity)) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_qq_uninstall);
            msg.arg1 = ISocialType.SOCIAL_QQ;
            handler.sendMessage(msg);
            return;
        }
        //获取分享类型
        if (shareDataBean.shareType == null) {
            shareType = 0;
        } else {
            shareType = shareDataBean.shareType.get(ISocialType.SOCIAL_QQ);
        }
        Bundle bundle = getShareData(shareDataBean);
        //特殊处理
        if (shareType == QQShareHelper.TYPE_IMAGE) {
            if (TextUtils.isEmpty(bundle.getString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL))) {
                Message msg = Message.obtain();
                msg.obj = activity.getString(R.string.social_error_image_not_found);
                msg.arg1 = ISocialType.SOCIAL_QQ;
                handler.sendMessage(msg);
                return;
            }
        }
        //分享到QQ
        tencent.shareToQQ(activity, bundle, shareListener);
    }

    /**
     * 获得需要传递给QQ的分享数据
     */
    private Bundle getShareData(ShareDataBean shareDataBean) {
        Bundle bundle = new Bundle();
        switch (shareType) {
            case QQShareHelper.TYPE_IMAGE_TEXT:
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareDataBean.appName);
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareDataBean.shareTitle);
                bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDataBean.shareDesc);
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareDataBean.shareImage);
                bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareDataBean.shareUrl);
                break;
            case QQShareHelper.TYPE_IMAGE:
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareDataBean.appName);
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, ImageUtil.getLocalImagePath(parentDir, shareDataBean.shareImage));
                break;
            case QQShareHelper.TYPE_MUSIC:
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareDataBean.appName);
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareDataBean.shareTitle);
                bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDataBean.shareDesc);
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareDataBean.shareImage);
                bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareDataBean.shareUrl);
                bundle.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, shareDataBean.shareMusicUrl);
                break;
            case QQShareHelper.TYPE_APP:
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
                bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareDataBean.appName);
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareDataBean.shareTitle);
                bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDataBean.shareDesc);
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareDataBean.shareImage);
                break;
        }
        return bundle;
    }

    /**
     * QQ开放平台需要
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
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
     * QQ的分享监听器
     */
    private IUiListener shareListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if (shareCallback != null) {
                shareCallback.onSuccess(ISocialType.SOCIAL_QQ, o.toString());
            }
            Utils.finish(activity, needFinishActivity);
        }

        @Override
        public void onError(UiError uiError) {
            if (shareCallback != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("errorCode", uiError.errorCode);
                    jsonObject.put("errorMessage", uiError.errorMessage);
                    jsonObject.put("errorDetail", uiError.errorDetail);
                } catch (NullPointerException | JSONException e) {
                    e.printStackTrace();
                }
                shareCallback.onError(ISocialType.SOCIAL_QQ, jsonObject.toString());
            }
            Utils.finish(activity, needFinishActivity);
        }

        @Override
        public void onCancel() {
            if (shareCallback != null) {
                shareCallback.onCancel(ISocialType.SOCIAL_QQ);
            }
            Utils.finish(activity, needFinishActivity);
        }
    };

}
