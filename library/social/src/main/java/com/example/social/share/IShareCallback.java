package com.example.social.share;

/**
 * 分享结果回调
 */
public interface IShareCallback {

    void onSuccess(int socialType, String msg);

    void onError(int socialType, String msg);

    void onCancel(int socialType);

}
