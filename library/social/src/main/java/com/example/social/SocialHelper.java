package com.example.social;

import android.app.Activity;
import android.content.Intent;

import com.example.social.auth.AuthActivity;
import com.example.social.auth.AuthHelper;
import com.example.social.auth.IAuthCallback;
import com.example.social.dialog.SocialTypeBean;
import com.example.social.pay.IPayCallback;
import com.example.social.pay.PayActivity;
import com.example.social.pay.PayHelper;
import com.example.social.pay.WXPayBean;
import com.example.social.share.IShareCallback;
import com.example.social.share.ShareActivity;
import com.example.social.share.ShareDataBean;
import com.example.social.share.ShareHelper;
import com.example.social.util.Utils;

import java.util.ArrayList;

/**
 * 基本信息初始化工具类
 */
public class SocialHelper {

    private ShareHelper shareHelper;
    private AuthHelper authHelper;
    private PayHelper payHelper;

    private IShareCallback shareCallback;
    private IAuthCallback authCallback;
    private IPayCallback payCallback;

    //单例引用
    private volatile static SocialHelper INSTANCE;

    //构造函数私有化
    private SocialHelper() {
    }

    //获取单例
    public static SocialHelper get() {
        if (INSTANCE == null) {
            synchronized (SocialHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SocialHelper();
                }
            }
        }
        return INSTANCE;
    }

    public ShareHelper getShareHelper() {
        if (shareHelper == null) {
            shareHelper = new ShareHelper();
        }
        return shareHelper;
    }

    public AuthHelper getAuthHelper() {
        if (authHelper == null) {
            authHelper = new AuthHelper();
        }
        return authHelper;
    }

    public PayHelper getPayHelper() {
        if (payHelper == null) {
            payHelper = new PayHelper();
        }
        return payHelper;
    }

    public IShareCallback getShareCallback() {
        return shareCallback;
    }

    public IAuthCallback getAuthCallback() {
        return authCallback;
    }

    public IPayCallback getPayCallback() {
        return payCallback;
    }

    public void share(Activity activity, SocialTypeBean socialTypeBean, ShareDataBean shareDataBean, IShareCallback shareCallback) {
        share(activity, socialTypeBean, shareDataBean, false, shareCallback);
    }

    public void share(Activity activity, SocialTypeBean socialTypeBean, ShareDataBean shareDataBean, boolean needFinishActivity, IShareCallback shareCallback) {
        if (Utils.isFastClick()) {
            return;
        }
        this.shareCallback = shareCallback;
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("SocialTypeBean", socialTypeBean);
        intent.putExtra("ShareDataBean", shareDataBean);
        intent.putExtra("needFinishActivity", needFinishActivity);
        activity.startActivity(intent);
    }

    public void share(Activity activity, ArrayList<SocialTypeBean> socialTypeBeans, ShareDataBean shareDataBean, IShareCallback shareCallback) {
        share(activity, socialTypeBeans, shareDataBean, false, shareCallback);
    }

    public void share(Activity activity, ArrayList<SocialTypeBean> socialTypeBeans, ShareDataBean shareDataBean, boolean needFinishActivity, IShareCallback shareCallback) {
        if (Utils.isFastClick()) {
            return;
        }
        this.shareCallback = shareCallback;
        Intent intent = new Intent(activity, ShareActivity.class);
        intent.putExtra("SocialTypeBeans", socialTypeBeans);
        intent.putExtra("ShareDataBean", shareDataBean);
        intent.putExtra("needFinishActivity", needFinishActivity);
        activity.startActivity(intent);
    }

    public void auth(Activity activity, SocialTypeBean socialTypeBean, IAuthCallback authCallback) {
        auth(activity, socialTypeBean, false, authCallback);
    }

    public void auth(Activity activity, SocialTypeBean socialTypeBean, boolean needFinishActivity, IAuthCallback authCallback) {
        if (Utils.isFastClick()) {
            return;
        }
        this.authCallback = authCallback;
        Intent intent = new Intent(activity, AuthActivity.class);
        intent.putExtra("SocialTypeBean", socialTypeBean);
        intent.putExtra("needFinishActivity", needFinishActivity);
        activity.startActivity(intent);
    }

    public void auth(Activity activity, ArrayList<SocialTypeBean> socialTypeBeans, IAuthCallback authCallback) {
        auth(activity, socialTypeBeans, false, authCallback);
    }

    public void auth(Activity activity, ArrayList<SocialTypeBean> socialTypeBeans, boolean needFinishActivity, IAuthCallback authCallback) {
        if (Utils.isFastClick()) {
            return;
        }
        this.authCallback = authCallback;
        Intent intent = new Intent(activity, AuthActivity.class);
        intent.putExtra("SocialTypeBeans", socialTypeBeans);
        intent.putExtra("needFinishActivity", needFinishActivity);
        activity.startActivity(intent);
    }

    public void pay(Activity activity, SocialTypeBean socialTypeBean, WXPayBean wxPayBean, String orderInfo, IPayCallback payCallback) {
        pay(activity, socialTypeBean, wxPayBean, orderInfo, false, payCallback);
    }

    public void pay(Activity activity, SocialTypeBean socialTypeBean, WXPayBean wxPayBean, String orderInfo, boolean needFinishActivity, IPayCallback payCallback) {
        if (Utils.isFastClick()) {
            return;
        }
        this.payCallback = payCallback;
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("SocialTypeBean", socialTypeBean);
        intent.putExtra("WXPayBean", wxPayBean);
        intent.putExtra("OrderInfo", orderInfo);
        intent.putExtra("needFinishActivity", needFinishActivity);
        activity.startActivity(intent);
    }

    public void pay(Activity activity, ArrayList<SocialTypeBean> socialTypeBeans, WXPayBean wxPayBean, String orderInfo, IPayCallback payCallback) {
        pay(activity, socialTypeBeans, wxPayBean, orderInfo, false, payCallback);
    }

    public void pay(Activity activity, ArrayList<SocialTypeBean> socialTypeBeans, WXPayBean wxPayBean, String orderInfo, boolean needFinishActivity, IPayCallback payCallback) {
        if (Utils.isFastClick()) {
            return;
        }
        this.payCallback = payCallback;
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("SocialTypeBeans", socialTypeBeans);
        intent.putExtra("WXPayBean", wxPayBean);
        intent.putExtra("OrderInfo", orderInfo);
        intent.putExtra("needFinishActivity", needFinishActivity);
        activity.startActivity(intent);
    }

    private String qqAppId;

    private String wxAppId;
    private String wxAppSecret;

    private String wbAppId;
    private String wbRedirectUrl;

    public String getQqAppId() {
        return qqAppId;
    }

    public SocialHelper setQqAppId(String qqAppId) {
        this.qqAppId = qqAppId;
        return this;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public SocialHelper setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
        return this;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public SocialHelper setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
        return this;
    }

    public String getWbAppId() {
        return wbAppId;
    }

    public SocialHelper setWbAppId(String wbAppId) {
        this.wbAppId = wbAppId;
        return this;
    }

    public String getWbRedirectUrl() {
        return wbRedirectUrl;
    }

    public SocialHelper setWbRedirectUrl(String wbRedirectUrl) {
        this.wbRedirectUrl = wbRedirectUrl;
        return this;
    }

}
