package com.example.learn.social;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.learn.R;
import com.example.social.ISocialType;
import com.example.social.SocialHelper;
import com.example.social.dialog.SocialTypeBean;
import com.example.social.share.IShareCallback;
import com.example.social.share.QQShareHelper;
import com.example.social.share.ShareDataBean;
import com.example.social.share.WBShareHelper;
import com.example.social.share.WXShareHelper;
import com.example.social.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class SocialSecondActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_second);
    }

    /**
     * 通过分享弹框分享
     */
    public void jump2Share(View view) {
        if (Utils.isFastClick()) {
            return;
        }

        ArrayList<SocialTypeBean> socialTypeBeans = new ArrayList<>();
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_SESSION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_TIMELINE));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SMS));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COPY));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_REFRESH));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_QQ));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WB));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_WX_MINIPROGRAM));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_ALIPAY));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_COLLECTION));
        socialTypeBeans.add(new SocialTypeBean(ISocialType.SOCIAL_SHOW_ALL));

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

        SocialHelper.get().share(this, socialTypeBeans, shareDataBean, new IShareCallback() {
            @Override
            public void onSuccess(int socialType, String msg) {
                Toast.makeText(SocialSecondActivity.this, "SecondActivity onSuccess, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int socialType, String msg) {
                Toast.makeText(SocialSecondActivity.this, "SecondActivity onError, socialType = " + socialType + ", msg = " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(int socialType) {
                Toast.makeText(SocialSecondActivity.this, "SecondActivity onCancel, socialType = " + socialType, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
