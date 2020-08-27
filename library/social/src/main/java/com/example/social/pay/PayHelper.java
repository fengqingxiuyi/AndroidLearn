package com.example.social.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.social.SocialHelper;

/**
 * 支付入口类
 */
public class PayHelper {

    private WXPayHelper wxPayHelper;
    private AliPayHelper aliPayHelper;

    /**
     * 发起微信支付
     */
    public void payWX(Activity activity, WXPayBean wxPayBean, IPayCallback payCallback, boolean needFinishActivity) {
        if (wxPayHelper == null) {
            wxPayHelper = new WXPayHelper(activity, SocialHelper.get().getWxAppId(), SocialHelper.get().getWxAppSecret());
        }
        wxPayHelper.pay(wxPayBean, payCallback, needFinishActivity);
    }

    /**
     * 发起支付宝支付
     */
    public void payAlipay(Activity activity, String orderInfo, IPayCallback payCallback, boolean needFinishActivity) {
        if (aliPayHelper == null) {
            aliPayHelper = new AliPayHelper(activity);
        }
        aliPayHelper.pay(orderInfo, payCallback, needFinishActivity);
    }

    /**
     * 微信授权，在微信回调到WXEntryActivity的onResp方法中调用
     *
     * @param success false表示失败，true表示成功
     * @param msg     消息内容
     */
    public void sendPayBroadcast(Context context, boolean success, String msg) {
        Intent intent = new Intent(WXPayHelper.ACTION_WX_PAY_RECEIVER);
        intent.putExtra(WXPayHelper.KEY_WX_PAY_RESULT, success);
        intent.putExtra(WXPayHelper.KEY_WX_PAY_MSG, msg);
        context.sendBroadcast(intent);
    }

    public void onDestroy() {
        if (wxPayHelper != null) {
            wxPayHelper.onDestroy();
            wxPayHelper = null;
        }
        if (aliPayHelper != null) {
            aliPayHelper.onDestroy();
            aliPayHelper = null;
        }
    }
}
