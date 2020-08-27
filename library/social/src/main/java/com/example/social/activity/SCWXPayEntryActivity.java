package com.example.social.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.social.SocialHelper;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class SCWXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "SCWXPayEntryActivity";

    private IWXAPI wxapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String wxAppId = SocialHelper.get().getWxAppId();
        wxapi = WXAPIFactory.createWXAPI(this, wxAppId, true);
        wxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d(TAG, baseResp.errCode + baseResp.errStr);
        //微信支付
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                SocialHelper.get().getPayHelper().sendPayBroadcast(this, true, getErrorMsg("支付", baseResp));
            } else {
                SocialHelper.get().getPayHelper().sendPayBroadcast(this, false, getErrorMsg("支付", baseResp));
            }
        }
        //关闭页面
        onBackPressed();
    }

    /**
     * https://open.yixin.im/resource/document/android_sdk_doc/im/yixin/sdk/api/BaseResp.ErrCode.html
     */
    private String getErrorMsg(String prefix, BaseResp baseResp) {
        if (baseResp == null) {
            return null;
        }
        if (TextUtils.isEmpty(baseResp.errStr)) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                return prefix + "成功";
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_COMM) {
                return prefix + "错误";
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                return prefix + "取消";
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_SENT_FAILED) {
                return prefix + "失败";
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
                return prefix + "拒绝";
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_UNSUPPORT) {
                return prefix + "不支持错误";
            } else if (baseResp.errCode == BaseResp.ErrCode.ERR_BAN) {
                return prefix + "签名错误";
            }
        } else {
            return baseResp.errStr;
        }
        return null;
    }

}
