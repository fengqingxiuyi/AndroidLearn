package com.example.learn.social;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.learn.R;
import com.example.learn.social.ali.OrderInfoUtil2_0;
import com.example.social.ISocialType;
import com.example.social.SocialHelper;
import com.example.social.auth.IAuthCallback;
import com.example.social.dialog.SocialTypeBean;
import com.example.social.pay.IPayCallback;
import com.example.social.pay.WXPayBean;
import com.example.social.share.IShareCallback;
import com.example.social.share.QQShareHelper;
import com.example.social.share.ShareDataBean;
import com.example.social.share.WBShareHelper;
import com.example.social.share.WXShareHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocialActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        initPermission();
    }

    /**
     * 6.0以上需要申请权限
     */
    private void initPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        //申请读写权限和电话权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    0);
            return;
        }
        //申请读写权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
            return;
        }
        //申请电话权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    0);
            return;
        }
    }

    /**
     * 通过分享弹框分享
     */
    public void jump2Share(View view) {
        ShareDataBean shareDataBean = new ShareDataBean();
        HashMap<Integer, Integer> shareTypeList = new HashMap<>();
        shareTypeList.put(ISocialType.SOCIAL_WX_SESSION, WXShareHelper.TYPE_WEB);
        shareTypeList.put(ISocialType.SOCIAL_WX_TIMELINE, WXShareHelper.TYPE_WEB);
        shareTypeList.put(ISocialType.SOCIAL_WX_MINIPROGRAM, WXShareHelper.TYPE_MINIPROGRAM);
        shareTypeList.put(ISocialType.SOCIAL_QQ, QQShareHelper.TYPE_IMAGE_TEXT);
        shareTypeList.put(ISocialType.SOCIAL_WB, WBShareHelper.TYPE_TEXT);
        shareDataBean.shareType = shareTypeList;
        shareDataBean.shareTitle = "百度一下，你就知道";
        shareDataBean.shareDesc = "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。";
        shareDataBean.shareImage = "https://www.baidu.com/img/bd_logo1.png";
        shareDataBean.shareUrl = "https://www.baidu.com/";
        shareDataBean.shareMiniType = 0; //小程序类型 - 正式版:0，测试版:1，体验版:2
        shareDataBean.shareMiniAppId = "gh_64c734bc4b8d"; //小程序AppId
        shareDataBean.shareMiniPage = "pages/fitting-room/index"; //小程序页面地址

        ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_TIMELINE));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SMS));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COPY));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_REFRESH));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_CUSTOM, "https://img.ezprice.com.tw/is/c.rimg.com.tw/s1/4/7e/29/21628111029801_843_s.jpg", "自定义图标需要集成图片库"));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WB));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_MINIPROGRAM));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_ALIPAY));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COLLECTION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SHOW_ALL));

        SocialHelper.get().share(this, socialTypeBeans, shareDataBean, new IShareCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "MainActivity onSuccess, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "MainActivity onError, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(SocialActivity.this, "MainActivity onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 直接分享到QQ
     */
    public void jump2ShareQQ(View view) {
        ShareDataBean shareDataBean = new ShareDataBean();
        HashMap<Integer, Integer> shareTypeList = new HashMap<>();
        shareTypeList.put(ISocialType.SOCIAL_WX_SESSION, WXShareHelper.TYPE_TEXT);
        shareTypeList.put(ISocialType.SOCIAL_QQ, QQShareHelper.TYPE_IMAGE_TEXT);
        shareTypeList.put(ISocialType.SOCIAL_WB, WBShareHelper.TYPE_TEXT);
        shareDataBean.shareType = shareTypeList;
        shareDataBean.shareTitle = "百度一下，你就知道";
        shareDataBean.shareDesc = "全球最大的中文搜索引擎、致力于让网民更便捷地获取信息，找到所求。百度超过千亿的中文网页数据库，可以瞬间找到相关的搜索结果。";
        shareDataBean.shareImage = "https://www.baidu.com/img/bd_logo1.png";
        shareDataBean.shareUrl = "https://www.baidu.com/";
        shareDataBean.shareMiniType = 0;
        shareDataBean.shareMiniPage = "小程序页面地址";

        SocialTypeBean socialTypeBean = new SocialTypeBean();
        socialTypeBean.socialType = ISocialType.SOCIAL_QQ;

        SocialHelper.get().share(this, socialTypeBean, shareDataBean, new IShareCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "MainActivity onSuccess, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "MainActivity onError, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(SocialActivity.this, "MainActivity onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 通过授权弹框授权
     */
    public void jump2Auth(View view) {
        ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WB));

        SocialHelper.get().auth(this, socialTypeBeans, true, new IAuthCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "onSuccess, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "onError, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(SocialActivity.this, "onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 直接授权到QQ
     */
    public void jump2AuthQQ(View view) {
        SocialTypeBean socialTypeBean = new SocialTypeBean();
        socialTypeBean.socialType = ISocialType.SOCIAL_QQ;

        SocialHelper.get().auth(this, socialTypeBean, new IAuthCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "onSuccess, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "onError, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(SocialActivity.this, "onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018070760519541";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEpAIBAAKCAQEAkZF3xOWipwfu52zJniPA3iRpXbqW8LhOrETusHkponznhbTP\n" +
            "GCQ3AxdM8IIThSYETkNA/Lg/w0xmDmowC72yEyPZ8P66R+hX5QJNEovsx/MxmJEv\n" +
            "DldDKnTkqRvVj75LTUDAU9aYRlYYuYef+WQB2YlVjm/ZGTdRzM6Cvx0xvFEOyMZ7\n" +
            "JVm7vEE3hg8WA3+e63JP11W1ZSZUgPT7krAPRilgPG7MWgeThPfeBpelP80cHlqY\n" +
            "B90EPYDWMikgQoAzB6fcFuRch2jyHS7VVTi8BBNhp8LAaidyTSYGMg8u9wvGT32X\n" +
            "hoUEr1hMJp5FPHBUSoU/O01nVBwhdAjk3kme5wIDAQABAoIBACK+4SKrWU6bMygk\n" +
            "LF/F0vRzJn9L2Vlt9MFj1sFiulBhlfWxOblec48WUbQaqEQg1ehEX0+zPu0D4FxS\n" +
            "dlNnHQk6hnvioV4mlExCZ1kk8wirAMyU4vF+XLbWnfnLfgdIebrf2MhqMYCgblhi\n" +
            "ignCGDrvb++GvQAq1yiWplitxEBZkJCwVPWaq/wvC7hQmMajUItSCb5nFS91nHif\n" +
            "sdkSE+Hd3/I5BqO5NGM4EaLBMcwzeX2zUSboJOPn4mPYE7xsQ67izDsjbYfiPKPB\n" +
            "ktimdkra4VEWtT7SzMNUrZznLyNymcXbMwJmHiOjECumQJb/y9vpmjevdRejQjIH\n" +
            "8310bKECgYEAweIJ/OL+atve4M2pn2rHmcamRgEEPLQwQfRUZAtBM670Jecor0Rm\n" +
            "aZRv1pEqRvy8NNVYfyN9ZhgL58hqJ+WXkPKuG+3GWcf4PmuRKXCQW2OxAwrD8aMT\n" +
            "lpMb5Rt5YHmQMSA+BkdzvsztUmykCBsArNXR2tFW1JGSvWqNAME3CDcCgYEAwDS7\n" +
            "+at54Foj7HSbUcmZnFS4lEd0DsiUV0dy+XpwuWNuvPEWP3BkqPmsCOsHqoiQeY3T\n" +
            "p8XEDdI+YaT/HK90W2X91wpGRj+6/brXpLBZg8YN0w1ChW/NLxrhnC1myfJZvGuz\n" +
            "nWVX7ct8q1vF1WupgdqAMy7O+SezRZwIDM6aZtECgYAD63sCNuCn7mwSJXZRd8oz\n" +
            "TLXd4nVgeNAk9TijkSqvTdE31vWWnY5oggzEzMiLCN8RPmL8pw4I+2F6PS4QQm8P\n" +
            "CikE/YiuFgHp2SjlKpZwkpJomlYd04s41x6QQj7w+WQ16g32+IjZibEeon2qgElC\n" +
            "NUx53ROhVpQd6kKRilZ/fwKBgQCPbkrgU9BNVbb9Gz9U10akXKypKnE3HwNj5lOs\n" +
            "TGyWS9c1dAuQJ8VWO4rf1NQRtHtr2iSaTICzd7NJlqExvNTt57x3npk2jxRxh96o\n" +
            "bVk/dqqWVozPmtDZWAwISfEsvoesjgqpaBknwPQ1w/RsAT2rQ2zrxQB6+d9UEM4J\n" +
            "EJw0IQKBgQCGi1oRV8r9CB4rfzadTfUce0HqsdrlQ+iH8eP6mJhpnW9dwhiXStia\n" +
            "V6xUtz+5PEHaRbxXN5Xg+zPU5yK+jupjxR2lBTCpv2uHJTiuRvumx6yGxXvDWxDY\n" +
            "7C8rKlwuAgMFMPkpA6MTNxZN2IxPRUoiJs3n9hher8cs16rj9ohHuw==";
    public static final String RSA_PRIVATE = "";

    /**
     * 通过支付弹框支付
     */
    public void jump2Pay(View view) {
        ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_ALIPAY));

        WXPayBean wxPayBean = new WXPayBean();
        wxPayBean.partnerId = "1900000109";
        wxPayBean.prepayId = "WX1217752501201407033233368018";
        wxPayBean.nonceStr = "Sign=WXPay";
        wxPayBean.timeStamp = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";
        wxPayBean.packageValue = "1412000000";
        wxPayBean.sign = "C380BEC2BFD727A4B6845133519F3AD6";

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        SocialHelper.get().pay(this, socialTypeBeans, wxPayBean, orderInfo, new IPayCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "onSuccess, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "onError, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(SocialActivity.this, "onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 直接支付到微信
     */
    public void jump2PayWX(View view) {
        SocialTypeBean socialTypeBean = new SocialTypeBean();
        socialTypeBean.socialType = ISocialType.SOCIAL_WX_SESSION;

        WXPayBean wxPayBean = new WXPayBean();
        wxPayBean.partnerId = "1900000109";
        wxPayBean.prepayId = "WX1217752501201407033233368018";
        wxPayBean.nonceStr = "Sign=WXPay";
        wxPayBean.timeStamp = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";
        wxPayBean.packageValue = "1412000000";
        wxPayBean.sign = "C380BEC2BFD727A4B6845133519F3AD6";

        SocialHelper.get().pay(this, socialTypeBean, wxPayBean, "", new IPayCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "onSuccess, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(SocialActivity.this, "onError, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(SocialActivity.this, "onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 跳转到第二个Activity
     */
    public void jump2SecondActivity(View view) {
        startActivity(new Intent(this, SocialSecondActivity.class));
    }
}
