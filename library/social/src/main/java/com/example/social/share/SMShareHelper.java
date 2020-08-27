package com.example.social.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.social.ISocialType;
import com.example.social.R;

import java.io.File;

/**
 * 短信分享帮助类
 */
public class SMShareHelper {

    //上下文
    private Activity activity;
    //分享结果回调
    private IShareCallback shareCallback;
    //图片缓存的父目录
    private File parentDir;
    //是否需要finishActivity
    private boolean needFinishActivity;

    /**
     * 初始化短信
     */
    public SMShareHelper(Activity activity, File parentDir) {
        this.activity = activity;
        this.parentDir = parentDir;
    }

    /**
     * 具体的分享逻辑
     */
    public void share(final ShareDataBean shareDataBean, IShareCallback shareCallback, Handler handler, boolean needFinishActivity) {
        this.shareCallback = shareCallback;
        this.needFinishActivity = needFinishActivity;
        //判断数据源是否为空
        if (shareDataBean == null) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_sms_share_data);
            msg.arg1 = ISocialType.SOCIAL_SMS;
            handler.sendMessage(msg);
            return;
        }
        //分享到短信或彩信
        if (TextUtils.isEmpty(shareDataBean.shareImage)) {
            shareSms(shareDataBean);
            return;
        }
        if (shareDataBean.shareImage.startsWith("http")) {
            String fileName = shareDataBean.shareImage.substring(shareDataBean.shareImage.lastIndexOf("/") + 1);
            if (TextUtils.isEmpty(fileName) || parentDir == null) {
                shareSms(shareDataBean);
                return;
            }
            final File file = new File(parentDir, fileName);
            if (file.exists()) {
                shareMms(shareDataBean, "file://" + file.getAbsolutePath());
            } else {
                boolean success = ImageUtil.downloadImage(file, shareDataBean.shareImage);
                if (success) {
                    shareMms(shareDataBean, "file://" + file.getAbsolutePath());
                } else {
                    shareSms(shareDataBean);
                }
            }
        } else {
            shareMms(shareDataBean, shareDataBean.shareImage);
        }
    }

    /**
     * 分享短信
     */
    private void shareSms(ShareDataBean shareDataBean) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
        intent.putExtra("sms_body", shareDataBean.shareTitle + "【" + shareDataBean.shareDesc + "】 链接 " + shareDataBean.shareUrl);
        activity.startActivity(intent);
    }

    /**
     * 分享彩信
     */
    private void shareMms(ShareDataBean shareDataBean, String localImageUrl) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, shareDataBean.shareTitle);
        intent.putExtra("subject", shareDataBean.shareTitle);
        intent.putExtra("sms_body", shareDataBean.shareTitle + "【" + shareDataBean.shareDesc + "】 链接 " + shareDataBean.shareUrl);
        intent.putExtra(Intent.EXTRA_TEXT, shareDataBean.shareTitle + "【" + shareDataBean.shareDesc + "】 链接 " + shareDataBean.shareUrl);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(localImageUrl));
        intent.setType("image/*");
        activity.startActivity(intent);
    }

}
