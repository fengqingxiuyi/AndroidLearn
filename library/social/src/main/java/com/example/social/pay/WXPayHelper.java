package com.example.social.pay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.example.social.ISocialType;
import com.example.social.R;
import com.example.social.SocialHelper;
import com.example.social.util.Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付帮助类
 * <p>
 * 相关文档：
 * 1、APP端开发步骤 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5
 * 2、调起支付接口 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12
 * 3、Demo下载 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=11_1
 */
public class WXPayHelper {

    //静态常量
    public static final String ACTION_WX_PAY_RECEIVER = "ACTION_WX_PAY_RECEIVER";
    public static final String KEY_WX_PAY_RESULT = "KEY_WX_PAY_RESULT";
    public static final String KEY_WX_PAY_MSG = "KEY_WX_PAY_MSG";

    //上下文
    private Activity activity;
    //
    private IWXAPI wxapi;
    //支付结果回调
    private IPayCallback payCallback;
    //是否需要finishActivity
    private boolean needFinishActivity;

    /**
     * 初始化微信
     */
    public WXPayHelper(Activity activity, String appId, String appSecret) {
        this.activity = activity;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }
        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
        //
        activity.registerReceiver(wxPayReceiver, new IntentFilter(WXPayHelper.ACTION_WX_PAY_RECEIVER));
    }

    /**
     * 具体的支付逻辑
     */
    public void pay(WXPayBean wxPayBean, IPayCallback payCallback, boolean needFinishActivity) {
        this.payCallback = payCallback;
        this.needFinishActivity = needFinishActivity;
        //NPE校验
        if (wxPayBean == null) {
            if (payCallback != null) {
                payCallback.onError(ISocialType.SOCIAL_WX_SESSION, activity.getString(R.string.social_error_wx_pay_data));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        //判断是否安装微信
        if (!wxapi.isWXAppInstalled()) {
            if (payCallback != null) {
                payCallback.onError(ISocialType.SOCIAL_WX_SESSION, activity.getString(R.string.social_error_wx_uninstall));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        //开始微信支付
        PayReq request = new PayReq();
        request.appId = SocialHelper.get().getWxAppId();
        request.partnerId = wxPayBean.partnerId;
        request.prepayId = wxPayBean.prepayId;
        request.packageValue = wxPayBean.packageValue;
        request.nonceStr = wxPayBean.nonceStr;
        request.timeStamp = wxPayBean.timeStamp;
        request.sign = wxPayBean.sign;
        wxapi.sendReq(request);
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            if (wxPayReceiver != null) {
                activity.unregisterReceiver(wxPayReceiver);
            }
            activity = null;
        }
    }

    /**
     * 微信的支付监听器
     */
    public BroadcastReceiver wxPayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (payCallback == null) {
                Utils.finish(activity, needFinishActivity);
                return;
            }
            String msg = intent.getStringExtra(WXPayHelper.KEY_WX_PAY_MSG);
            boolean success = intent.getBooleanExtra(WXPayHelper.KEY_WX_PAY_RESULT, false);
            if (success) {
                payCallback.onSuccess(ISocialType.SOCIAL_WX_SESSION, msg);
            } else {
                payCallback.onError(ISocialType.SOCIAL_WX_SESSION, msg);
            }
            Utils.finish(activity, needFinishActivity);
        }
    };
}
