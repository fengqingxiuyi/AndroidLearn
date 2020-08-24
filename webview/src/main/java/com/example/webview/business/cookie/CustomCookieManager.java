package com.example.webview.business.cookie;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.example.utils.LogUtil;
import com.example.utils.encrypt.AesUtils;

import org.json.JSONObject;

import java.util.Date;

/**
 * cookie管理类
 */
public class CustomCookieManager implements ICookieConfig {

    private static volatile CustomCookieManager cookieManager;

    public static CustomCookieManager getInstance() {
        if (cookieManager == null) {
            synchronized (CustomCookieManager.class) {
                if (cookieManager == null) {
                    cookieManager = new CustomCookieManager();
                }
            }
        }
        return cookieManager;
    }

    private CustomCookieManager() {
    }

    /**
     * 去除cookie
     */
    public void removeCookie() {
        //清空腾讯cookie
        CookieManager.getInstance().removeAllCookie();
        CookieManager.getInstance().removeSessionCookie();

        //清空原生cookie
        CookieManager.getInstance().removeAllCookie();
        CookieManager.getInstance().removeSessionCookie();
    }

    /**
     * 同步cookie
     */
    public void synCookies(final Context context) {
        //用户信息
        String info = "app_user_info=" + getUserInfoCookie();
        synCookie(context, ICookieConfig.releaseDomain, info);
        synCookie(context, ICookieConfig.debugDomain, info);
    }

    /**
     * 获取用户信息cookie字符串
     */
    private String getUserInfoCookie() {
        String userInfo = "";
        JSONObject infoObject = new JSONObject();
        //此处往infoObject中添加自定义的用户信息
        try {
            userInfo = AesUtils.Encrypt(infoObject.toString(), h5key);
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return userInfo;
    }

    private void synCookie(Context context, String url, String info) {
        //设置用户信息cookie
        setCookies(context, url, info);
        //设置失效时间
        Date date = new Date();
        date.setYear(date.getYear() + 1);
        setCookies(context, url, "expires=" + date.toGMTString());
    }

    /**
     * 设置Cookie
     *
     * @param cookie 格式：uid=21233 如需设置多个，需要多次调用
     */
    private void setCookies(final Context context, final String url, final String cookie) {
        try {
            //原生cookieManager
            CookieSyncManager nativeCookieSyncManager = CookieSyncManager.createInstance(context);
            CookieManager nativeCookieManager = CookieManager.getInstance();
            nativeCookieManager.setAcceptCookie(true);
            nativeCookieManager.setCookie(url, cookie);//cookies是在HttpClient中获得的cookie
            if (null != nativeCookieSyncManager)
                nativeCookieSyncManager.sync();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }
}
