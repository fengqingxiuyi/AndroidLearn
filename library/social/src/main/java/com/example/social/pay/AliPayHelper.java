package com.example.social.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.example.social.ISocialType;
import com.example.social.R;
import com.example.social.util.Utils;

import java.util.Map;

/**
 * 支付宝支付帮助类
 * <p>
 * 相关文档：
 * 1、开发文档 /  资源下载 /  概览 https://docs.open.alipay.com/54/cyz7do/
 * 2、开发文档 /  App支付 /  产品介绍 https://docs.open.alipay.com/204
 */
public class AliPayHelper {

    private static final String TAG = "AliPayHelper";

    //上下文
    private Activity activity;
    //支付结果回调
    private IPayCallback payCallback;
    //是否需要finishActivity
    private boolean needFinishActivity;
    //
    private AliPayHandler aliPayHandler;

    public AliPayHelper(Activity activity) {
        this.activity = activity;
        aliPayHandler = new AliPayHandler();
    }

    public void pay(final String orderInfo, final IPayCallback payCallback, final boolean needFinishActivity) {
        this.payCallback = payCallback;
        this.needFinishActivity = needFinishActivity;
        //NPE校验
        if (TextUtils.isEmpty(orderInfo)) {
            if (payCallback != null) {
                payCallback.onError(ISocialType.SOCIAL_ALIPAY, activity.getString(R.string.social_error_ali_pay_data));
            }
            Utils.finish(activity, needFinishActivity);
            return;
        }
        ;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    PayTask alipay = new PayTask(activity);
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    Log.d(TAG, " msp " + result.toString());

                    Message msg = new Message();
                    msg.obj = result;
                    aliPayHandler.sendMessage(msg);
                } catch (Throwable e) {
                    e.printStackTrace();
                    if (payCallback != null) {
                        payCallback.onError(ISocialType.SOCIAL_ALIPAY, activity.getString(R.string.social_error_ali_pay_data));
                    }
                    Utils.finish(activity, needFinishActivity);
                }
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }
        if (aliPayHandler != null) {
            aliPayHandler.removeCallbacksAndMessages(null);
            aliPayHandler = null;
        }
    }

    class AliPayHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            try {
                AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。
                 */
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) { //请求处理成功
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    payCallback.onSuccess(ISocialType.SOCIAL_ALIPAY, null);
                } else if (TextUtils.equals(resultStatus, "6001")) { //用户中途取消
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    payCallback.onCancel(ISocialType.SOCIAL_ALIPAY);
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    payCallback.onError(ISocialType.SOCIAL_ALIPAY, null);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                if (payCallback != null) {
                    payCallback.onError(ISocialType.SOCIAL_ALIPAY, activity.getString(R.string.social_error_ali_pay_data));
                }
            } finally {
                Utils.finish(activity, needFinishActivity);
            }
        }
    }

}
