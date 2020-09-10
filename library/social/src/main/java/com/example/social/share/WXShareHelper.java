package com.example.social.share;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.social.ISocialType;
import com.example.social.R;
import com.example.social.util.Utils;
import com.example.utils.view.ImageUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

/**
 * 微信分享帮助类
 * <p>
 * 相关文档：
 * 1、分享与收藏功能开发文档（Android应用） https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317340&token=&lang=zh_CN
 */
public class WXShareHelper {

    //分享文本
    public static final int TYPE_TEXT = 0;
    //分享图片
    public static final int TYPE_IMAGE = 1;
    //分享音乐
    public static final int TYPE_MUSIC = 2;
    //分享视频
    public static final int TYPE_VIDEO = 3;
    //分享网页
    public static final int TYPE_WEB = 4;
    //分享小程序
    public static final int TYPE_MINIPROGRAM = 5;

    //
    public static final String ACTION_WX_SHARE_RECEIVER = "ACTION_WX_SHARE_RECEIVER";
    public static final String KEY_WX_SHARE_RESULT = "KEY_WX_SHARE_RESULT";
    public static final String KEY_WX_SHARE_MSG = "KEY_WX_SHARE_MSG";

    //上下文
    private Activity activity;
    //
    private IWXAPI wxapi;
    //分享结果回调
    private IShareCallback shareCallback;
    //图片缓存的父目录
    private File parentDir;
    //微信或朋友圈或小程序
    private int socialType;
    //分享类型
    private int shareType;
    //是否需要finishActivity
    private boolean needFinishActivity;

    /**
     * 初始化微信
     */
    public WXShareHelper(Activity activity, String appId, String appSecret, File parentDir) {
        this.activity = activity;
        this.parentDir = parentDir;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            throw new RuntimeException("Wechat's appId or appSecret is empty!");
        }
        wxapi = WXAPIFactory.createWXAPI(activity, appId, true);
        wxapi.registerApp(appId);
        //
        activity.registerReceiver(wxShareReceiver, new IntentFilter(WXShareHelper.ACTION_WX_SHARE_RECEIVER));
    }

    /**
     * 具体的分享逻辑
     */
    public void share(int socialType, ShareDataBean shareDataBean, IShareCallback shareCallback, Handler handler, boolean needFinishActivity) {
        this.socialType = socialType;
        this.shareCallback = shareCallback;
        this.needFinishActivity = needFinishActivity;
        //判断数据源是否为空
        if (shareDataBean == null) {
            Message msg = Message.obtain();
            if (socialType == ISocialType.SOCIAL_WX_SESSION) {
                msg.obj = "微信" + activity.getString(R.string.social_error_wx_share_data);
            } else if (socialType == ISocialType.SOCIAL_WX_TIMELINE) {
                msg.obj = "朋友圈" + activity.getString(R.string.social_error_wx_share_data);
            } else {
                msg.obj = "小程序" + activity.getString(R.string.social_error_wx_share_data);
            }
            msg.arg1 = socialType;
            handler.sendMessage(msg);
            return;
        }
        //判断是否安装微信
        if (!wxapi.isWXAppInstalled()) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wx_uninstall);
            msg.arg1 = socialType;
            handler.sendMessage(msg);
            return;
        }
        //是否分享到朋友圈，微信4.2以下不支持朋友圈
        if (socialType == ISocialType.SOCIAL_WX_TIMELINE && wxapi.getWXAppSupportAPI() < 0x21020001) {
            Message msg = Message.obtain();
            msg.obj = activity.getString(R.string.social_error_wx_version_low);
            msg.arg1 = socialType;
            handler.sendMessage(msg);
            return;
        }
        //获取分享类型
        if (shareDataBean.shareType == null) {
            shareType = 0;
        } else {
            shareType = shareDataBean.shareType.get(socialType);
        }
        //需要传递给微信的分享数据
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = getShareMessage(req, shareDataBean);
        if (req.message == null) {
            Message msg = Message.obtain();
            if (socialType == ISocialType.SOCIAL_WX_SESSION) {
                msg.obj = "微信" + activity.getString(R.string.social_error_wx_share_data);
            } else if (socialType == ISocialType.SOCIAL_WX_TIMELINE) {
                msg.obj = "朋友圈" + activity.getString(R.string.social_error_wx_share_data);
            } else {
                msg.obj = "小程序" + activity.getString(R.string.social_error_wx_share_data);
            }
            msg.arg1 = socialType;
            handler.sendMessage(msg);
            return;
        }
        if (socialType == ISocialType.SOCIAL_WX_TIMELINE) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        //分享到scene（微信或朋友圈或小程序）
        wxapi.sendReq(req);
    }

    /**
     * 需要传递给微信的分享数据
     */
    private WXMediaMessage getShareMessage(SendMessageToWX.Req req, ShareDataBean shareDataBean) {
        WXMediaMessage msg = new WXMediaMessage();
        boolean success = false;
        switch (shareType) {
            case WXShareHelper.TYPE_TEXT:
                success = addText(req, msg, shareDataBean.shareDesc);
                break;
            case WXShareHelper.TYPE_IMAGE:
                success = addImage(req, msg, shareDataBean.shareImage);
                break;
            case WXShareHelper.TYPE_MUSIC:
                success = addMusic(req, msg, shareDataBean.shareMusicUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                break;
            case WXShareHelper.TYPE_VIDEO:
                success = addVideo(req, msg, shareDataBean.shareVideoUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                break;
            case WXShareHelper.TYPE_WEB:
                success = addWeb(req, msg, shareDataBean.shareUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage);
                break;
            case WXShareHelper.TYPE_MINIPROGRAM:
                success = addMiniProgram(req, msg, shareDataBean.shareUrl, shareDataBean.shareTitle, shareDataBean.shareDesc, shareDataBean.shareImage, shareDataBean.shareMiniType, shareDataBean.shareMiniAppId, shareDataBean.shareMiniPage);
                break;
        }
        if (!success) {
            return null;
        }
        return msg;
    }

    private boolean addText(SendMessageToWX.Req req, WXMediaMessage msg, String text) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        msg.mediaObject = textObj;
        msg.description = textObj.text;

        req.transaction = buildTransaction("text");
        return true;
    }

    private boolean addImage(SendMessageToWX.Req req, WXMediaMessage msg, String image) {
        String imagePath = ImageUtil.getLocalImagePath(parentDir, image);
        if (TextUtils.isEmpty(imagePath) || imagePath.length() > 512) {
            return false;
        }
        File file = new File(imagePath);
        if (!file.exists() || file.length() == 0L || file.length() > 10485760L) {
            return false;
        }
        WXImageObject imageObject = new WXImageObject();
        imageObject.imagePath = imagePath;
        msg.mediaObject = imageObject;
        byte[] thumbData = ImageUtil.getImageByte(imagePath, 32768);
        if (thumbData == null) {
            return false;
        }
        msg.thumbData = thumbData;

        req.transaction = buildTransaction("image");
        return true;
    }

    private boolean addMusic(SendMessageToWX.Req req, WXMediaMessage msg, String musicUrl, String title, String desc, String image) {
        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = musicUrl;

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = buildTransaction("music");
        return true;
    }

    private boolean addVideo(SendMessageToWX.Req req, WXMediaMessage msg, String videoUrl, String title, String desc, String image) {
        WXVideoObject videoObject = new WXVideoObject();
        videoObject.videoUrl = videoUrl;

        msg.mediaObject = videoObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = buildTransaction("video");
        return true;
    }

    private boolean addWeb(SendMessageToWX.Req req, WXMediaMessage msg, String url, String title, String desc, String image) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = url;

        msg.mediaObject = webpageObject;
        if (addTitleSummaryAndThumb(msg, title, desc, image)) return false;

        req.transaction = buildTransaction("webpage");
        return true;
    }

    private boolean addMiniProgram(SendMessageToWX.Req req, WXMediaMessage msg, String url, String title, String desc, String image, int miniType, String miniAppId, String miniPage) {
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = url; //兼容低版本的网页链接
        miniProgramObj.miniprogramType = miniType; //正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = miniAppId; //小程序原始id
        miniProgramObj.path = miniPage; //小程序页面路径

        msg.mediaObject = miniProgramObj;
        if (addTitleSummaryAndThumb(msg, title, desc, image, 131072)) return false;

        req.transaction = buildTransaction("miniProgram");
        return true;
    }

    private boolean addTitleSummaryAndThumb(WXMediaMessage msg, String title, String desc, String image) {
        return addTitleSummaryAndThumb(msg, title, desc, image, 32768);
    }

    /**
     * 当有设置缩略图但是找不到的时候阻止分享
     */
    private boolean addTitleSummaryAndThumb(WXMediaMessage msg, String title, String desc, String image, int maxSize) {
        msg.title = title; //消息title
        msg.description = desc; //消息desc
        byte[] thumbData = ImageUtil.getImageByte(image, maxSize);
        if (thumbData == null) {
            return true;
        }
        msg.thumbData = thumbData; //消息封面图片 小程序大小限制：128k，其它分享大小限制32k
        return false;
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (activity != null) {
            if (wxShareReceiver != null) {
                activity.unregisterReceiver(wxShareReceiver);
            }
            activity = null;
        }
    }

    /**
     * 用于唯一标识一个请求
     */
    private String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 微信的分享监听器
     */
    public BroadcastReceiver wxShareReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (shareCallback == null) {
                Utils.finish(activity, needFinishActivity);
                return;
            }
            String msg = intent.getStringExtra(WXShareHelper.KEY_WX_SHARE_MSG);
            boolean success = intent.getBooleanExtra(WXShareHelper.KEY_WX_SHARE_RESULT, false);
            if (success) {
                if (socialType == ISocialType.SOCIAL_WX_SESSION) {
                    shareCallback.onSuccess(ISocialType.SOCIAL_WX_SESSION, TextUtils.isEmpty(msg) ? null : "微信" + msg);
                } else if (socialType == ISocialType.SOCIAL_WX_TIMELINE) {
                    shareCallback.onSuccess(ISocialType.SOCIAL_WX_TIMELINE, TextUtils.isEmpty(msg) ? null : "朋友圈" + msg);
                } else if (socialType == ISocialType.SOCIAL_WX_MINIPROGRAM) {
                    shareCallback.onSuccess(ISocialType.SOCIAL_WX_MINIPROGRAM, TextUtils.isEmpty(msg) ? null : "小程序" + msg);
                }
            } else {
                if (socialType == ISocialType.SOCIAL_WX_SESSION) {
                    shareCallback.onError(ISocialType.SOCIAL_WX_SESSION, TextUtils.isEmpty(msg) ? null : "微信" + msg);
                } else if (socialType == ISocialType.SOCIAL_WX_TIMELINE) {
                    shareCallback.onError(ISocialType.SOCIAL_WX_TIMELINE, TextUtils.isEmpty(msg) ? null : "朋友圈" + msg);
                } else if (socialType == ISocialType.SOCIAL_WX_MINIPROGRAM) {
                    shareCallback.onError(ISocialType.SOCIAL_WX_MINIPROGRAM, TextUtils.isEmpty(msg) ? null : "小程序" + msg);
                }
            }
            Utils.finish(activity, needFinishActivity);
        }
    };
}
