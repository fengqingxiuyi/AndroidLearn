package com.example.social.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.example.social.SocialHelper;
import com.example.social.util.Utils;

import java.util.concurrent.ExecutorService;

/**
 * 授权入口类
 */
public class AuthHelper {

    //线程池
    private ExecutorService executorService;

    //静态常量
    private static final int TYPE_AUTH_WX = 1;
    private static final int TYPE_AUTH_QQ = 2;
    private static final int TYPE_AUTH_WB = 3;

    //各模块帮助类
    private WXAuthHelper wxAuthHelper;
    private QQAuthHelper qqAuthHelper;
    private WBAuthHelper wbAuthHelper;

    //上下文
    private Activity activity;
    //授权结果回调
    private IAuthCallback authCallback;
    //是否需要finishActivity
    private boolean needFinishActivity;

    //当前授权类型
    private int currAuthType;
    //解决有些错误回调在子线程的问题
    private Handler authHandler;

    /**
     * 微信授权
     */
    public void authWX(Activity activity, IAuthCallback authCallback, boolean needFinishActivity) {
        this.activity = activity;
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        if (authHandler == null) {
            authHandler = new AuthHandler();
        }
        currAuthType = TYPE_AUTH_WX;
        if (wxAuthHelper == null) {
            wxAuthHelper = new WXAuthHelper(activity, SocialHelper.get().getWxAppId(), SocialHelper.get().getWxAppSecret());
        }
        wxAuthHelper.auth(authCallback, needFinishActivity, executorService, authHandler);
    }

    /**
     * QQ授权
     */
    public void authQQ(Activity activity, IAuthCallback authCallback, boolean needFinishActivity) {
        currAuthType = TYPE_AUTH_QQ;
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        if (authHandler == null) {
            authHandler = new AuthHandler();
        }
        currAuthType = TYPE_AUTH_QQ;
        if (qqAuthHelper == null) {
            qqAuthHelper = new QQAuthHelper(activity, SocialHelper.get().getQqAppId());
        }
        qqAuthHelper.auth(authCallback, needFinishActivity, executorService, authHandler);
    }

    /**
     * 微博授权
     */
    public void authWB(Activity activity, IAuthCallback authCallback, boolean needFinishActivity) {
        this.activity = activity;
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        if (authHandler == null) {
            authHandler = new AuthHandler();
        }
        currAuthType = TYPE_AUTH_WB;
        if (wbAuthHelper == null) {
            wbAuthHelper = new WBAuthHelper(activity, SocialHelper.get().getWbAppId(), SocialHelper.get().getWbRedirectUrl());
        }
        wbAuthHelper.auth(authCallback, needFinishActivity, executorService, authHandler);
    }

    /**
     * 微信授权，在微信回调到WXEntryActivity的onResp方法中调用
     *
     * @param success false表示失败，true表示成功
     * @param msg     消息内容
     */
    public void sendAuthBroadcast(Context context, boolean success, String msg, String code) {
        Intent intent = new Intent(WXAuthHelper.ACTION_WX_AUTH_RECEIVER);
        intent.putExtra(WXAuthHelper.KEY_WX_AUTH_RESULT, success);
        intent.putExtra(WXAuthHelper.KEY_WX_AUTH_MSG, msg);
        intent.putExtra(WXAuthHelper.KEY_WX_AUTH_CODE, code);
        context.sendBroadcast(intent);
    }

    /**
     * QQ授权和分享以及微博授权都需要在其当前的activity的onActivityResult中调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqAuthHelper != null && currAuthType == TYPE_AUTH_QQ) {
            qqAuthHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (wbAuthHelper != null && currAuthType == TYPE_AUTH_WB) {
            wbAuthHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onDestroy() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if (authHandler != null) {
            authHandler.removeCallbacksAndMessages(null);
            authHandler = null;
        }
        if (wxAuthHelper != null) {
            wxAuthHelper.onDestroy();
            wxAuthHelper = null;
        }
        if (qqAuthHelper != null) {
            qqAuthHelper.onDestroy();
            qqAuthHelper = null;
        }
        if (wbAuthHelper != null) {
            wbAuthHelper.onDestroy();
            wbAuthHelper = null;
        }
    }

    /**
     * 解决有些回调在子线程的问题
     */
    private class AuthHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonStr = (String) msg.obj;
            if (authCallback != null) {
                authCallback.onSuccess(msg.arg1, jsonStr);
            }
            Utils.finish(activity, needFinishActivity);
        }
    }

}
