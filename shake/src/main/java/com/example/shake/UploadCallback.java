package com.example.shake;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author fqxyi
 * @date 2017/4/11
 */
public interface UploadCallback {

    void onFailure(Call call, IOException e);

    void onResponse(Call call, Response response) throws IOException;

}
