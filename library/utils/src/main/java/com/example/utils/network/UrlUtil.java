package com.example.utils.network;

import android.net.Uri;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * URL操作类，例如：编解码操作
 */
public class UrlUtil {

    /**
     * 以UTF-8的编码形式 编码 http url
     */
    public static String encodeUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        Uri uri = Uri.parse(url);
        if (uri == null) {
            return "";
        }
        // 拆分uri path，逐个编码
        List<String> pathSegments = uri.getPathSegments();
        List<String> encodedPathSegments = new ArrayList<>();
        for (String uriPathSegment : pathSegments) {
            if (TextUtils.isEmpty(uriPathSegment)) {
                continue;
            }
            try {
                encodedPathSegments.add(URLEncoder.encode(uriPathSegment, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
        // 拆分uri parameters，逐个编码
        Map<String, String> encodedParameters = new HashMap<>();
        Set<String> parameterNames = uri.getQueryParameterNames();
        for (String parameterName : parameterNames) {
            if (TextUtils.isEmpty(parameterName)) {
                continue;
            }
            String parameterValue = uri.getQueryParameter(parameterName);
            if (TextUtils.isEmpty(parameterValue)) {
                parameterValue = "";
            }
            try {
                encodedParameters.put(parameterName, URLEncoder.encode(parameterValue, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
        // 组合编码后的uri
        StringBuilder builder = new StringBuilder();
        builder.append(url.substring(0, url.indexOf(uri.getEncodedPath())));
        for (String uriEncodedPathSegment : encodedPathSegments) {
            if (TextUtils.isEmpty(uriEncodedPathSegment)) {
                continue;
            }
            builder.append("/");
            builder.append(uriEncodedPathSegment);
        }
        builder.append("?");
        for (Map.Entry<String, String> entry : encodedParameters.entrySet()) {
            if (entry == null) {
                continue;
            }
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }
        if (builder.lastIndexOf("&") == builder.length() - 1) {
            builder.delete(builder.lastIndexOf("&"), builder.length());
        }
        if (builder.lastIndexOf("?") == builder.length() - 1) {
            builder.delete(builder.lastIndexOf("?"), builder.length());
        }
        // 返回url
        return builder.toString();
    }

    /**
     * 以UTF-8的编码形式 解码 http url
     */
    public static String decodeUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        Uri uri = Uri.parse(url);
        if (uri == null) {
            return "";
        }
        // 拆分uri path，逐个解码
        List<String> pathSegments = uri.getPathSegments();
        List<String> decodedPathSegments = new ArrayList<>();
        for (String uriPathSegment : pathSegments) {
            if (TextUtils.isEmpty(uriPathSegment)) {
                continue;
            }
            try {
                decodedPathSegments.add(URLDecoder.decode(uriPathSegment, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
        // 拆分uri parameters，逐个解码
        Map<String, String> decodedParameters = new HashMap<>();
        Set<String> parameterNames = uri.getQueryParameterNames();
        for (String parameterName : parameterNames) {
            if (TextUtils.isEmpty(parameterName)) {
                continue;
            }
            String parameterValue = uri.getQueryParameter(parameterName);
            if (TextUtils.isEmpty(parameterValue)) {
                parameterValue = "";
            }
            try {
                decodedParameters.put(parameterName, URLDecoder.decode(parameterValue, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
        // 组合解码后的uri
        StringBuilder builder = new StringBuilder();
        builder.append(url.substring(0, url.indexOf(uri.getEncodedPath())));
        for (String uriEncodedPathSegment : decodedPathSegments) {
            if (TextUtils.isEmpty(uriEncodedPathSegment)) {
                continue;
            }
            builder.append("/");
            builder.append(uriEncodedPathSegment);
        }
        builder.append("?");
        for (Map.Entry<String, String> entry : decodedParameters.entrySet()) {
            if (entry == null) {
                continue;
            }
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }
        if (builder.lastIndexOf("&") == builder.length() - 1) {
            builder.delete(builder.lastIndexOf("&"), builder.length());
        }
        if (builder.lastIndexOf("?") == builder.length() - 1) {
            builder.delete(builder.lastIndexOf("?"), builder.length());
        }
        // 返回url
        return builder.toString();
    }

}
