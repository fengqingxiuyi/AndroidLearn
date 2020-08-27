package com.example.social.dialog;

import com.example.social.ISocialType;
import com.example.social.R;

/**
 * 弹框需要的社会化工具类
 */
public class SocialDialogUtil {

    public static int getIcon(int type) {
        switch (type) {
            case ISocialType.SOCIAL_WX_SESSION: //微信
                return R.drawable.social_icon_wechat;
            case ISocialType.SOCIAL_WX_TIMELINE: //朋友圈
                return R.drawable.social_icon_wechatmoments;
            case ISocialType.SOCIAL_SMS: //短信
                return R.drawable.social_icon_shortmessage;
            case ISocialType.SOCIAL_COPY: //复制
                return R.drawable.social_icon_copy;
            case ISocialType.SOCIAL_REFRESH: //刷新
                return R.drawable.social_icon_refresh;
            case ISocialType.SOCIAL_QQ: //QQ
                return R.drawable.social_icon_qq;
            case ISocialType.SOCIAL_WB: //微博
                return R.drawable.social_icon_sina;
            case ISocialType.SOCIAL_WX_MINIPROGRAM: //微信小程序
                return R.drawable.social_icon_wechat;
            case ISocialType.SOCIAL_ALIPAY: //支付宝
                return R.drawable.social_icon_alipay;
            case ISocialType.SOCIAL_COLLECTION: //收藏
                return R.drawable.social_icon_collection_normal;
            case ISocialType.SOCIAL_SHOW_ALL: //查看全部
                return R.drawable.social_icon_show_all;
        }
        return R.drawable.social_icon_refresh;
    }

    public static String getName(int type) {
        switch (type) {
            case ISocialType.SOCIAL_WX_SESSION: //微信好友
                return "微信好友";
            case ISocialType.SOCIAL_WX_TIMELINE: //朋友圈
                return "朋友圈";
            case ISocialType.SOCIAL_SMS: //短信
                return "短信";
            case ISocialType.SOCIAL_COPY: //复制
                return "复制链接";
            case ISocialType.SOCIAL_REFRESH: //刷新
                return "刷新";
            case ISocialType.SOCIAL_QQ: //QQ
                return "QQ";
            case ISocialType.SOCIAL_WB: //微博
                return "微博";
            case ISocialType.SOCIAL_WX_MINIPROGRAM: //微信小程序
                return "微信小程序";
            case ISocialType.SOCIAL_ALIPAY: //支付宝
                return "支付宝";
            case ISocialType.SOCIAL_COLLECTION: //收藏
                return "收藏";
            case ISocialType.SOCIAL_SHOW_ALL: //查看全部
                return "查看全部";
        }
        return "刷新";
    }

}
