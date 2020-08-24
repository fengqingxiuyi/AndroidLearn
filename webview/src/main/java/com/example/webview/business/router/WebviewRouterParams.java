package com.example.webview.business.router;

import android.net.Uri;
import android.text.TextUtils;

import com.example.utils.encrypt.Base64Util;

/**
 * 路由参数封装类
 */
public class WebviewRouterParams {

    private String url;
    //类型
    private String type;
    //目标方法
    private String target;
    //解密过的参数
    private String params;
    //回调方法
    private String callback;

    public WebviewRouterParams(String url) {
        this.url = url;
        if (TextUtils.isEmpty(url)) {
            return;
        }
        queryParams(Uri.parse(url));
    }

    private void queryParams(Uri uri) {
        try {
            //类型
            type = uri.getQueryParameter("type");
            //目标方法
            target = uri.getQueryParameter("target");
            //加密过的参数
            String encodeParams = uri.getQueryParameter("params");
            //解密后的参数
            if (!TextUtils.isEmpty(encodeParams)) {
                params = new String(Base64Util.decode(encodeParams.replace(" ", "+")));
            }
            //回调方法
            this.callback = uri.getQueryParameter("callback");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
