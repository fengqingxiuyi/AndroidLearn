package com.example.shake

import okhttp3.Call
import okhttp3.Response
import java.io.IOException

/**
 * @author fqxyi
 * @date 2017/4/11
 */
interface UploadCallback {
    fun onFailure(call: Call, e: IOException)

    @Throws(IOException::class)
    fun onResponse(call: Call, response: Response)
}