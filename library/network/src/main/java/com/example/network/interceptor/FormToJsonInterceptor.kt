package com.example.network.interceptor

import com.example.network.global.NetGlobal
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * @author fqxyi
 * @date 2018/2/27
 * 将form表单数据以json的形式传给服务端
 */
class FormToJsonInterceptor : Interceptor {

    companion object {
        private const val METHOD_GET = "GET"
        private const val METHOD_POST = "POST"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = request.url()
        NetGlobal.requestUrl = httpUrl.toString()
        if (METHOD_GET == request.method()) { // GET方法
            NetGlobal.requestParams = getParamsGet(httpUrl)
        } else if (METHOD_POST == request.method()) { // POST方法
            val requestBody = request.body()
            val jsonObjectStr = getParamsPostForm(requestBody)
            NetGlobal.requestParams = jsonObjectStr
            //构造新的json格式请求
            if (requestBody is FormBody) {
                val body = RequestBody.create(
                    MediaType.parse("application/json; charset=UTF-8"),
                    jsonObjectStr
                )
                return chain.proceed(
                    request.newBuilder()
                        .post(body)
                        .build()
                )
            }
        } else {
            NetGlobal.requestParams = "{}"
        }
        return chain.proceed(request)
    }

    /**
     * 获取 GET 请求参数
     */
    private fun getParamsGet(httpUrl: HttpUrl): String {
        val paramKeys = httpUrl.queryParameterNames()
        val jsonObject = JSONObject()
        for (key in paramKeys) {
            val value = httpUrl.queryParameter(key)
            jsonPut(jsonObject, key, value)
        }
        return jsonObject.toString()
    }

    /**
     * 获取 POST FORM表单 请求参数
     */
    private fun getParamsPostForm(requestBody: RequestBody?): String {
        return if (requestBody is FormBody) {
            val jsonObject = JSONObject()
            for (i in 0 until requestBody.size()) {
                jsonPut(jsonObject, requestBody.name(i), requestBody.value(i))
            }
            jsonObject.toString()
        } else {
            "{}"
        }
    }

    private fun jsonPut(
        jsonObject: JSONObject,
        key: String,
        value: String?
    ) {
        try {
            jsonObject.put(key, value)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}