package com.example.social.auth;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.social.ISocialType;
import com.example.social.R;
import com.example.social.util.Utils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 微信授权帮助类
 * <p>
 * 相关文档：
 * 1、移动应用微信登录开发指南 https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317851&token=&lang=zh_CN
 */
public class WXAuthHelper {

    //静态常量
    public static final String ACTION_WX_AUTH_RECEIVER = "ACTION_WX_AUTH_RECEIVER";
    public static final String KEY_WX_AUTH_RESULT = "KEY_WX_AUTH_RESULT";
    public static final String KEY_WX_AUTH_MSG = "KEY_WX_AUTH_MSG";
    public static final String KEY_WX_AUTH_CODE = "KEY_WX_AUTH_CODE";

    //上下文
    private Activity activity;
    //
    private String appId, appSecret;
    //
    private IWXAPI wxapi;
    //授权结果回调
    private IAuthCallback authCallback;
    //是否需要finishActivity
    private boolean needFinishActivity;
    //线程池
    private ExecutorService executorService;
    //解决有些错误回调在子线程的问题
    private Handler authHandler;

    /**
     * 初始化微信
     */
    public WXAuthHelper(Activity activity, String appId, String appSecret) {
        this.activity = activity;
        this.appId = appId;
        this.appSecret = appSecret;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }
        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
        //
        activity.registerReceiver(wxAuthReceiver, new IntentFilter(WXAuthHelper.ACTION_WX_AUTH_RECEIVER));
    }

    /**
     * 具体的授权逻辑
     */
    public void auth(IAuthCallback authCallback, boolean needFinishActivity, ExecutorService executorService, Handler authHandler) {
        this.authCallback = authCallback;
        this.needFinishActivity = needFinishActivity;
        this.executorService = executorService;
        this.authHandler = authHandler;
        //判断是否安装微信
        if (!wxapi.isWXAppInstalled()) {
            if (authCallback != null) {
                authCallback.onError(ISocialType.SOCIAL_WX_SESSION, activity.getString(R.string.social_error_wx_uninstall));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        //开始微信授权
        SendAuth.Req req = new SendAuth.Req();
        //应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
        // snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
        req.scope = "snsapi_userinfo";
        //重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节，该值会被微信原样返回，我们可以将其进行比对，防止别人的攻击。
        req.state = activity.getPackageName();
        wxapi.sendReq(req);
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            if (wxAuthReceiver != null) {
                activity.unregisterReceiver(wxAuthReceiver);
            }
            activity = null;
        }
    }

    /**
     * 微信的授权监听器
     */
    public BroadcastReceiver wxAuthReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (authCallback == null) {
                Utils.finish(activity, needFinishActivity);
                return;
            }
            String msg = intent.getStringExtra(WXAuthHelper.KEY_WX_AUTH_MSG);
            boolean success = intent.getBooleanExtra(WXAuthHelper.KEY_WX_AUTH_RESULT, false);
            if (success) {
                getAccessToken(intent.getStringExtra(WXAuthHelper.KEY_WX_AUTH_CODE));
            } else {
                authCallback.onError(ISocialType.SOCIAL_WX_SESSION, msg);
                Utils.finish(activity, needFinishActivity);
            }
        }
    };

    /**
     * 通过code获取access_token
     * http请求方式: GET
     * 正确的返回：
     * {
     * "access_token":"ACCESS_TOKEN", //接口调用凭证
     * "expires_in":7200, //access_token接口调用凭证超时时间，单位（秒）
     * "refresh_token":"REFRESH_TOKEN", //用户刷新access_token
     * "openid":"OPENID", //授权用户唯一标识
     * "scope":"SCOPE" //用户授权的作用域，使用逗号（,）分隔
     * }
     * 错误的返回：
     * {
     * "errcode":40029,"errmsg":"invalid code"
     * }
     */
    private void getAccessToken(final String code) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(1);
        }
        if (executorService.isShutdown()) {
            authCallback.onSuccess(ISocialType.SOCIAL_WX_SESSION, null);
            Utils.finish(activity, needFinishActivity);
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String jsonStr = "";
                try {
                    jsonStr = Utils.get(
                            "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                                    "appid=" + appId +
                                    "&secret=" + appSecret +
                                    "&code=" + code +
                                    "&grant_type=authorization_code"
                    );
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    jsonStr = getUserInfo(jsonObject.getString("access_token"), jsonObject.getString("openid"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (authHandler != null) {
                    Message msg = Message.obtain();
                    msg.obj = jsonStr;
                    msg.arg1 = ISocialType.SOCIAL_WX_SESSION;
                    authHandler.sendMessage(msg);
                } else {
                    Log.e("AuthHelper", "authHandler == null");
                    Utils.finish(activity, needFinishActivity);
                }
            }
        });
    }

    /**
     * 获取用户信息
     * 正确的返回：
     * {
     * "openid":"OPENID", //普通用户的标识，对当前开发者帐号唯一
     * "nickname":"NICKNAME", //普通用户昵称
     * "sex":1, //普通用户性别，1为男性，2为女性
     * "province":"PROVINCE", //普通用户个人资料填写的省份
     * "city":"CITY", //普通用户个人资料填写的城市
     * "country":"COUNTRY", //国家，如中国为CN
     * "headimgurl": "HEADIMGURL", //用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     * "privilege":[ //用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     * "PRIVILEGE1",
     * "PRIVILEGE2"
     * ],
     * "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL" //用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     * }
     * 错误的返回:
     * {
     * "errcode":40003,"errmsg":"invalid openid"
     * }
     */
    private String getUserInfo(String access_token, String openid) throws Exception {
        return Utils.get(
                "https://api.weixin.qq.com/sns/userinfo?" +
                        "access_token=" + access_token +
                        "&openid=" + openid
        );
    }

}
