package com.example.social.share;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.example.social.ISocialType;
import com.example.social.R;
import com.example.social.SocialHelper;
import com.example.social.util.Utils;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 分享入口类
 */
public class ShareHelper {

    private static final String TAG = "ShareHelper";

    //线程池
    private ExecutorService executorService;

    //上下文
    private Activity activity;
    //图片缓存的父目录
    private File parentDir;

    private WXShareHelper wxShareHelper;
    private SMShareHelper smShareHelper;
    private QQShareHelper qqShareHelper;
    private WBShareHelper wbShareHelper;

    //分享结果回调
    private IShareCallback shareCallback;
    //解决有些错误回调在子线程的问题
    private Handler shareHandler;
    //是否需要finishActivity
    private boolean needFinishActivity;

    public ShareHelper() {
        parentDir = new File(Environment.getExternalStorageDirectory(), "SocialComponent" + File.separator + "Share");
    }

    /**
     * 分享到微信或朋友圈或小程序
     */
    public void shareWX(final Activity activity, final int socialType, final ShareDataBean shareDataBean, final IShareCallback shareCallback, final boolean needFinishActivity) {
        this.activity = activity;
        this.shareCallback = shareCallback;
        this.needFinishActivity = needFinishActivity;
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        if (executorService.isShutdown()) {
            if (shareCallback != null) {
                shareCallback.onError(socialType, activity.getString(R.string.social_error_thread_shutdown));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        if (shareHandler == null) {
            shareHandler = new ShareHandler();
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (wxShareHelper == null) {
                    wxShareHelper = new WXShareHelper(activity, SocialHelper.get().getWxAppId(), SocialHelper.get().getWxAppSecret(), parentDir);
                }
                wxShareHelper.share(socialType, shareDataBean, shareCallback, shareHandler, needFinishActivity);
            }
        });
    }

    /**
     * 分享到短信
     */
    public void shareShortMessage(final Activity activity, final ShareDataBean shareDataBean, final IShareCallback shareCallback, final boolean needFinishActivity) {
        this.activity = activity;
        this.shareCallback = shareCallback;
        this.needFinishActivity = needFinishActivity;
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        if (executorService.isShutdown()) {
            if (shareCallback != null) {
                shareCallback.onError(ISocialType.SOCIAL_SMS, activity.getString(R.string.social_error_thread_shutdown));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        if (shareHandler == null) {
            shareHandler = new ShareHandler();
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (smShareHelper == null) {
                    smShareHelper = new SMShareHelper(activity, parentDir);
                }
                smShareHelper.share(shareDataBean, shareCallback, shareHandler, needFinishActivity);
            }
        });
    }

    /**
     * 复制
     */
    public void shareCopy(Activity activity, ShareDataBean shareDataBean, IShareCallback shareCallback, boolean needFinishActivity) {
        ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            // 将文本数据复制到剪贴板
            clipboardManager.setText(shareDataBean.shareUrl);
            if (shareCallback != null) {
                shareCallback.onSuccess(ISocialType.SOCIAL_COPY, "复制成功");
            }
        } else {
            if (shareCallback != null) {
                shareCallback.onError(ISocialType.SOCIAL_COPY, "复制失败");
            }
        }
        Utils.finish(activity, needFinishActivity);
    }

    /**
     * 刷新
     */
    public void shareRefresh(Activity activity, ShareDataBean shareDataBean, IShareCallback shareCallback, boolean needFinishActivity) {

    }

    /**
     * 自定义
     */
    public void shareCustom(Activity activity, ShareDataBean shareDataBean, IShareCallback shareCallback, boolean needFinishActivity) {

    }

    /**
     * 分享到QQ
     */
    public void shareQQ(final Activity activity, final ShareDataBean shareDataBean, final IShareCallback shareCallback, final boolean needFinishActivity) {
        this.activity = activity;
        this.shareCallback = shareCallback;
        this.needFinishActivity = needFinishActivity;
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        if (executorService.isShutdown()) {
            if (shareCallback != null) {
                shareCallback.onError(ISocialType.SOCIAL_QQ, activity.getString(R.string.social_error_thread_shutdown));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        if (shareHandler == null) {
            shareHandler = new ShareHandler();
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (qqShareHelper == null) {
                    qqShareHelper = new QQShareHelper(activity, SocialHelper.get().getQqAppId(), parentDir);
                }
                qqShareHelper.share(shareDataBean, shareCallback, shareHandler, needFinishActivity);
            }
        });
    }

    /**
     * 分享到微博
     */
    public void shareWB(final Activity activity, final ShareDataBean shareDataBean, final IShareCallback shareCallback, final boolean needFinishActivity) {
        this.activity = activity;
        this.shareCallback = shareCallback;
        this.needFinishActivity = needFinishActivity;
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        if (executorService.isShutdown()) {
            if (shareCallback != null) {
                shareCallback.onError(ISocialType.SOCIAL_WB, activity.getString(R.string.social_error_thread_shutdown));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        if (shareHandler == null) {
            shareHandler = new ShareHandler();
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (wbShareHelper == null) {
                    wbShareHelper = new WBShareHelper(activity, SocialHelper.get().getWbAppId(), SocialHelper.get().getWbRedirectUrl(), parentDir);
                }
                wbShareHelper.share(shareDataBean, shareCallback, shareHandler, needFinishActivity);
            }
        });
    }

    /**
     * 分享到支付宝
     */
    public void shareAlipay(Activity activity, ShareDataBean shareDataBean, IShareCallback shareCallback, boolean needFinishActivity) {

    }

    /**
     * 收藏
     */
    public void shareCollection(Activity activity, ShareDataBean shareDataBean, IShareCallback shareCallback, boolean needFinishActivity) {

    }

    /**
     * 查看全部
     */
    public void shareShowAll(Activity activity, ShareDataBean shareDataBean, IShareCallback shareCallback, boolean needFinishActivity) {

    }

    /**
     * QQ授权和分享以及微博授权都需要在其当前的activity的onActivityResult中调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqShareHelper != null) {
            qqShareHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 微博分享需要在其当前的activity中的onNewIntent中调用该方法
     */
    public void onNewIntent(Intent intent) {
        if (wbShareHelper != null) {
            wbShareHelper.onNewIntent(intent);
        }
    }

    /**
     * 微信分享，在微信回调到WXEntryActivity的onResp方法中调用
     *
     * @param success false表示失败，true表示成功
     * @param msg     消息内容
     */
    public void sendShareBroadcast(Context context, boolean success, String msg) {
        Intent intent = new Intent(WXShareHelper.ACTION_WX_SHARE_RECEIVER);
        intent.putExtra(WXShareHelper.KEY_WX_SHARE_RESULT, success);
        intent.putExtra(WXShareHelper.KEY_WX_SHARE_MSG, msg);
        context.sendBroadcast(intent);
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if (shareHandler != null) {
            shareHandler.removeCallbacksAndMessages(null);
            shareHandler = null;
        }
        if (wxShareHelper != null) {
            wxShareHelper.onDestroy();
            wxShareHelper = null;
        }
        if (qqShareHelper != null) {
            qqShareHelper.onDestroy();
            qqShareHelper = null;
        }
        if (wbShareHelper != null) {
            wbShareHelper.onDestroy();
            wbShareHelper = null;
        }
    }

    /**
     * 解决有些错误回调在子线程的问题
     */
    private class ShareHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String errorMsg = (String) msg.obj;
            int socialType = msg.arg1;
            if (shareCallback != null) {
                shareCallback.onError(socialType, errorMsg);
            }
            Utils.finish(activity, needFinishActivity);
        }
    }

}
