package com.example.social.auth;

/**
 * 授权结果回调
 */
public interface IAuthCallback {

    void onSuccess(int socialType, String msg);

    void onError(int socialType, String msg);

    void onCancel(int socialType);

}
