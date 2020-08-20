package com.example.shake;

import okhttp3.MediaType;

/**
 * @author fqxyi
 * @date 2017/4/11
 */
public interface ShakeSensorConstant {

    String URL = "";

    String WEBHOOK_TOKEN_UPLOAD_IMAGE = "";

    MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

}
