package com.example.social.pay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.social.ISocialType;
import com.example.social.SocialHelper;
import com.example.social.dialog.ItemClickListener;
import com.example.social.dialog.SocialDialog;
import com.example.social.dialog.SocialTypeBean;
import com.example.social.util.Utils;

import java.util.ArrayList;

public class PayActivity extends Activity {

    //社会化弹框
    private SocialDialog socialDialog;
    //支付入口类
    private PayHelper payHelper;
    //onResume的时候关闭activity使用
    private boolean fromCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromCreate = true;
        //初始化授权类型
        Intent intent = getIntent();
        if (intent == null) {
            Utils.toast(this, "社会化类型为空");
            finish();
            return;
        }
        ArrayList<SocialTypeBean> socialTypeBeans = (ArrayList<SocialTypeBean>) intent.getSerializableExtra("SocialTypeBeans");
        SocialTypeBean socialTypeBean = (SocialTypeBean) intent.getSerializableExtra("SocialTypeBean");
        final WXPayBean wxPayBean = (WXPayBean) intent.getSerializableExtra("WXPayBean");
        final String orderInfo = intent.getStringExtra("OrderInfo");
        final boolean needFinishActivity = intent.getBooleanExtra("needFinishActivity", false);
        //NPE校验
        if ((socialTypeBeans == null || socialTypeBeans.size() == 0) && socialTypeBean == null) {
            Utils.toast(this, "社会化类型为空");
            finish();
            return;
        }
        //创建支付入口类
        payHelper = SocialHelper.get().getPayHelper();
        if (socialTypeBeans != null && socialTypeBeans.size() > 0) {
            //显示授权弹框
            if (socialDialog == null) {
                socialDialog = new SocialDialog(this);
            }
            socialDialog.initSocialType(socialTypeBeans);
            socialDialog.setItemClickListener(new ItemClickListener() {
                @Override
                public void click(SocialTypeBean socialTypeBean, int position) {
                    initItemClick(socialTypeBean, wxPayBean, orderInfo, SocialHelper.get().getPayCallback(), needFinishActivity);
                }
            });
            socialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            socialDialog.show();
        } else if (socialTypeBean != null) {
            initItemClick(socialTypeBean, wxPayBean, orderInfo, SocialHelper.get().getPayCallback(), true);
        } else {
            Utils.toast(this, "分享初始化异常");
            finish();
        }
    }

    /**
     * 具体的item点击逻辑
     */
    private void initItemClick(SocialTypeBean socialTypeBean, WXPayBean wxPayBean, String orderInfo, IPayCallback payCallback, boolean needFinishActivity) {
        if (socialTypeBean == null) {
            return;
        }
        switch (socialTypeBean.socialType) {
            case ISocialType.SOCIAL_WX_SESSION: //微信
                payHelper.payWX(this, wxPayBean, payCallback, needFinishActivity);
                break;
            case ISocialType.SOCIAL_ALIPAY: //支付宝
                payHelper.payAlipay(this, orderInfo, payCallback, needFinishActivity);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!fromCreate && socialDialog == null) {
            finish();
        }
        fromCreate = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socialDialog != null) {
            socialDialog.dismiss();
            socialDialog = null;
        }
        if (payHelper != null) {
            payHelper.onDestroy();
            payHelper = null;
        }
    }


}
