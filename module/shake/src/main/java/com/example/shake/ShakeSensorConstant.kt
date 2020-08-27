package com.example.shake

import okhttp3.MediaType

/**
 * @author fqxyi
 * @date 2017/4/11
 */
interface ShakeSensorConstant {
    companion object {
        const val URL = ""

        const val WEBHOOK_TOKEN_UPLOAD_IMAGE = ""

        val MEDIA_TYPE_PNG: MediaType? = MediaType.parse("image/png")
        val MEDIA_TYPE_JSON: MediaType? = MediaType.parse("application/json; charset=utf-8")
    }
}