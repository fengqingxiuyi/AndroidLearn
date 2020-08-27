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
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * QQ授权帮助类
 * <p>
 * 相关文档：
 * 1、QQ登录和注销 http://wiki.open.qq.com/wiki/QQ%E7%99%BB%E5%BD%95%E5%92%8C%E6%B3%A8%E9%94%80
 * 2、获取用户信息 http://wiki.connect.qq.com/get_user_info
 * <p>
 * 问题：QQ授权100044问题，解决办法：本APP未上线，如果你申请的是“个人开发者”，请确保你创建APP所用的QQ帐号和你测试时用的登陆QQ号一致！
 */
public class QQAuthHelper {

    //上下文
    private Activity activity;
    //
    private final String appId;
    //
    private Tencent tencent;
    //授权结果回调
    private IAuthCallback authCallback;
    //是否需要finishActivity
    private boolean needFinishActivity;
    //线程池
    private ExecutorService executorService;
    //解决有些错误回调在子线程的问题
    private Handler authHandler;

    /**
     * 初始化QQ
     */
    public QQAuthHelper(Activity activity, String appId) {
        this.activity = activity;
        this.appId = appId;
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("QQ's appId is empty!");
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
    }

    /**
     * 具体的授权逻辑
     */
    public void auth(IAuthCallback authCallback, boolean needFinishActivity, ExecutorService executorService, Handler authHandler) {
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        this.executorService = executorService;
        this.authHandler = authHandler;
        //判断是否安装QQ
        if (!tencent.isQQInstalled(activity)) {
            if (authCallback != null) {
                authCallback.onError(ISocialType.SOCIAL_QQ, activity.getString(R.string.social_error_qq_uninstall));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        //开始QQ授权
        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", authListener);
        }
    }

    /**
     * QQ开放平台需要
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, authListener);
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
     * QQ的授权监听器
     */
    private IUiListener authListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            //判断返回数据是否为空
            if (o == null || TextUtils.isEmpty(o.toString())) {
                if (authCallback != null) {
                    authCallback.onSuccess(ISocialType.SOCIAL_QQ, null);
                }
                Utils.finish(activity, needFinishActivity);
                return;
            }
            //从返回数据中获取access_token和openid
            String access_token = "";
            String openid = "";
            try {
                JSONObject jsonObject = new JSONObject(o.toString());
                access_token = jsonObject.getString("access_token");
                openid = jsonObject.getString("openid");
            } catch (JSONException e) {
                if (authCallback != null) {
                    authCallback.onSuccess(ISocialType.SOCIAL_QQ, null);
                }
                Utils.finish(activity, needFinishActivity);
                return;
            }
            //创建线程池
            if (executorService == null) {
                executorService = Executors.newFixedThreadPool(1);
            }
            //判断线程池是否开启
            if (executorService.isShutdown()) {
                if (authCallback != null) {
                    authCallback.onSuccess(ISocialType.SOCIAL_QQ, null);
                }
                Utils.finish(activity, needFinishActivity);
                return;
            }
            //发起获取用户信息请求
            final String final_access_token = access_token;
            final String final_openid = openid;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String jsonStr = "";
                    try {
                        jsonStr = Utils.get(
                                "https://graph.qq.com/user/get_user_info?" +
                                        "access_token=" + final_access_token +
                                        "&oauth_consumer_key=" + appId +
                                        "&openid=" + final_openid
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    logout();
                    if (authHandler != null) {
                        Message msg = Message.obtain();
                        msg.obj = jsonStr;
                        msg.arg1 = ISocialType.SOCIAL_QQ;
                        authHandler.sendMessage(msg);
                    } else {
                        Log.e("AuthHelper", "authHandler == null");
                        Utils.finish(activity, needFinishActivity);
                    }
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            if (authCallback != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("errorCode", uiError.errorCode);
                    jsonObject.put("errorMessage", uiError.errorMessage);
                    jsonObject.put("errorDetail", uiError.errorDetail);
                } catch (NullPointerException | JSONException e) {
                    e.printStackTrace();
                }
                authCallback.onError(ISocialType.SOCIAL_QQ, jsonObject.toString());
            }
            logout();
            Utils.finish(activity, needFinishActivity);
        }

        @Override
        public void onCancel() {
            if (authCallback != null) {
                authCallback.onCancel(ISocialType.SOCIAL_QQ);
            }
            logout();
            Utils.finish(activity, needFinishActivity);
        }
    };

    /**
     * 调用QQ注销接口
     */
    private void logout() {
        if (tencent != null) {
            tencent.logout(activity);
        }
    }

}
