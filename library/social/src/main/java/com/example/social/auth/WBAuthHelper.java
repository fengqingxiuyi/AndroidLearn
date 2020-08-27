package com.example.social.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.social.ISocialType;
import com.example.social.R;
import com.example.social.util.Utils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 授权帮助类
 * <p>
 * 相关文档
 * 1、移动客户端接入 http://open.weibo.com/wiki/%E7%A7%BB%E5%8A%A8%E5%AE%A2%E6%88%B7%E7%AB%AF%E6%8E%A5%E5%85%A5#SDK.E6.8E.A5.E5.85.A5.E6.B5.81.E7.A8.8B
 * 2、微博SDK 4.1文档.pdf https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E6%96%B0%E6%96%87%E6%A1%A3/%E5%BE%AE%E5%8D%9ASDK%204.1%E6%96%87%E6%A1%A3.pdf
 * <p>
 * 问题：微博授权21338问题，未通过审核或者是签名不一致
 */
public class WBAuthHelper {

    //上下文
    private Activity activity;
    //
    private SsoHandler ssoHandler;
    //授权结果回调
    private IAuthCallback authCallback;
    //是否需要finishActivity
    private boolean needFinishActivity;
    //线程池
    private ExecutorService executorService;
    //解决有些错误回调在子线程的问题
    private Handler authHandler;

    /**
     * 初始化微博
     */
    public WBAuthHelper(Activity activity, String appId, String redirectUrl) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            throw new RuntimeException("WeBo's appId or redirectUrl is empty!");
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, ""));
    }

    /**
     * 具体的授权逻辑
     */
    public void auth(IAuthCallback authCallback, boolean needFinishActivity, ExecutorService executorService, Handler authHandler) {
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        this.executorService = executorService;
        this.authHandler = authHandler;
        //判断是否安装微博
        if (!WbSdk.isWbInstall(activity)) {
            if (authCallback != null) {
                authCallback.onError(ISocialType.SOCIAL_WB, activity.getString(R.string.social_error_wb_uninstall));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        //开始微博授权
        if (ssoHandler == null) {
            ssoHandler = new SsoHandler(activity);
        }
        ssoHandler.authorize(wbAuthCallback);
    }

    /**
     * 微博开放平台需要
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
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
     * 微博的授权监听器
     */
    private WbAuthListener wbAuthCallback = new WbAuthListener() {
        @Override
        public void onSuccess(final Oauth2AccessToken oauth2AccessToken) {
            if (oauth2AccessToken == null || !oauth2AccessToken.isSessionValid()) {
                if (authCallback != null) {
                    authCallback.onError(ISocialType.SOCIAL_WB, null);
                }
                Utils.finish(activity, needFinishActivity);
                return;
            }
            if (executorService == null) {
                executorService = Executors.newFixedThreadPool(1);
            }
            if (executorService.isShutdown()) {
                if (authCallback != null) {
                    authCallback.onSuccess(ISocialType.SOCIAL_WB, null);
                }
                Utils.finish(activity, needFinishActivity);
                return;
            }
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String jsonStr = "";
                    try {
                        jsonStr = Utils.get(
                                "https://api.weibo.com/2/users/show.json?" +
                                        "access_token=" + oauth2AccessToken.getToken() +
                                        "&uid=" + oauth2AccessToken.getUid()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (authHandler != null) {
                        Message msg = Message.obtain();
                        msg.obj = jsonStr;
                        msg.arg1 = ISocialType.SOCIAL_WB;
                        authHandler.sendMessage(msg);
                    } else {
                        Log.e("AuthHelper", "authHandler == null");
                        Utils.finish(activity, needFinishActivity);
                    }
                }
            });
        }

        @Override
        public void cancel() {
            if (authCallback != null) {
                authCallback.onCancel(ISocialType.SOCIAL_WB);
            }
            Utils.finish(activity, needFinishActivity);
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            if (authCallback != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("errorCode", wbConnectErrorMessage.getErrorCode());
                    jsonObject.put("errorMessage", wbConnectErrorMessage.getErrorMessage());
                } catch (NullPointerException | JSONException e) {
                    e.printStackTrace();
                }
                authCallback.onError(ISocialType.SOCIAL_WB, jsonObject.toString());
            }
            Utils.finish(activity, needFinishActivity);
        }
    };

}
