package com.example.shake;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author fqxyi
 * @date 2017/4/11
 * 摇一摇工具类
 */
public class ShakeSensorUtil {

    private static ShakeSensorUtil shakeSensorUtil = new ShakeSensorUtil();

    private ShakeSensorUtil() {
    }

    public static ShakeSensorUtil getInstance() {
        return shakeSensorUtil;
    }

    private OkHttpClient okHttpClient;

    private OkHttpClient WHTOkHttpClient;

    /**
     * 异步上传图片
     * @param url 接口地址
     * @param filePath 文件路径
     * @param uploadCallback 上传回调
     */
    public void postUploadImage(String url, String filePath, final UploadCallback uploadCallback) {
        if (TextUtils.isEmpty(url)) {
            Log.e("ScreenRecordUtil", "postAsyncFile url is NULL");
            return;
        }
        if (TextUtils.isEmpty(filePath)) {
            Log.e("ScreenRecordUtil", "postAsyncFile filePath is NULL");
            return;
        }
        if (null == uploadCallback) {
            Log.e("ShakeSensorUtil", "postAsyncFile uploadCallback is NULL");
            return;
        }
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", filePath.substring(filePath.lastIndexOf("/")+1),
                        RequestBody.create(ShakeSensorConstant.Companion.getMEDIA_TYPE_PNG(), new File(filePath)))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                uploadCallback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                uploadCallback.onResponse(call, response);
            }
        });
    }

    public void postUploadJson(String url, String json, final UploadCallback uploadCallback) {
        if (TextUtils.isEmpty(url)) {
            Log.e("ScreenRecordUtil", "postWebHookToken url is NULL");
            return;
        }
        if (TextUtils.isEmpty(json)) {
            Log.e("ScreenRecordUtil", "postWebHookToken json is NULL");
            return;
        }
        if (null == uploadCallback) {
            Log.e("ShakeSensorUtil", "postWebHookToken uploadCallback is NULL");
            return;
        }
        if (WHTOkHttpClient == null) {
            WHTOkHttpClient = new OkHttpClient();
        }
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(ShakeSensorConstant.Companion.getMEDIA_TYPE_JSON(), json))
                .build();
        WHTOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                uploadCallback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                uploadCallback.onResponse(call, response);
            }
        });
    }

    /**
     * <ul>
     * <li>分享任意类型的<b >单个</b>文件|不包含目录</li>
     * <li>[<b>经验证！可以发送任意类型的文件！！！</b>]</li>
     * <ul>
     *
     * @param context
     * @param uri Uri.from(file);
     *
     */
    public static void shareFile(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("subject", ""); //
        intent.putExtra("body", ""); // 正文
        intent.putExtra(Intent.EXTRA_STREAM, uri); // 添加附件，附件为file对象
        if (uri.toString().endsWith(".gz")) {
            intent.setType("application/x-gzip"); // 如果是gz使用gzip的mime
        } else if (uri.toString().endsWith(".txt")) {
            intent.setType("text/plain"); // 纯文本则用text/plain的mime
        } else {
            intent.setType("application/octet-stream"); // 其他的均使用流当做二进制数据来发送
        }
        context.startActivity(intent); // 调用系统的mail客户端进行发送
    }

}
