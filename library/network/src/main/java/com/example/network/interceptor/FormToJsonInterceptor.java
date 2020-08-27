package com.example.network.interceptor;

import androidx.annotation.NonNull;

import com.example.network.global.NetGlobal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 将form表单数据以json的形式传给服务端
 */
public class FormToJsonInterceptor implements Interceptor {

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl httpUrl = request.url();
        NetGlobal.requestUrl = httpUrl.toString();

        if (METHOD_GET.equals(request.method())) { // GET方法
            NetGlobal.requestParams = getParamsGet(httpUrl);
        } else if (METHOD_POST.equals(request.method())) { // POST方法
            RequestBody requestBody = request.body();
            String jsonObjectStr = getParamsPostForm(requestBody);
            NetGlobal.requestParams = jsonObjectStr;
            //构造新的json格式请求
            if (requestBody instanceof FormBody) {
                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json; charset=UTF-8"),
                        jsonObjectStr);
                return chain.proceed(request.newBuilder()
                        .post(body)
                        .build());
            }
        } else {
            NetGlobal.requestParams = "{}";
        }

        return chain.proceed(request);
    }

    /**
     * 获取 GET 请求参数
     */
    private String getParamsGet(HttpUrl httpUrl) {
        Set<String> paramKeys = httpUrl.queryParameterNames();
        JSONObject jsonObject = new JSONObject();
        for (String key : paramKeys) {
            String value = httpUrl.queryParameter(key);
            jsonPut(jsonObject, key, value);
        }
        return jsonObject.toString();
    }

    /**
     * 获取 POST FORM表单 请求参数
     */
    private String getParamsPostForm(RequestBody requestBody) {
        if (requestBody instanceof FormBody) {
            FormBody formBody = (FormBody) requestBody;
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < formBody.size(); i++) {
                jsonPut(jsonObject, formBody.name(i), formBody.value(i));
            }
            return jsonObject.toString();
        } else {
            return "{}";
        }
    }

    private void jsonPut(JSONObject jsonObject, String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
