package com.example.learn.ui.network.hello;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.common.network.API;
import com.example.common.ui.loading.LoadingUtil;
import com.example.learn.R;
import com.example.learn.ui.network.mvp.view.MvpActivity;
import com.example.network.RequestManager;
import com.example.network.bean.ErrorBean;
import com.example.network.callback.IResponseCallback;
import com.example.network.tag.ReqTag;
import com.example.ui.toast.ToastUtil;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 测试类
 */
public class NetworkHelloActivity extends BaseActivity {

    //api地址编辑框
    private EditText switchApiEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        //findView
        switchApiEdit = (EditText) findViewById(R.id.switch_api_edit);
    }

    public void startRequest(View view) {
        LoadingUtil.showLoading(activity);

        RequestManager.get().async(
                new ReqTag(1),
                RequestManager.get().create(NetworkHelloApiService.class).getData(1),
                new IResponseCallback<NetworkHelloBean>() {
                    @Override
                    public void onSuccess(@NonNull ReqTag reqTag, @NonNull NetworkHelloBean response) {
                        LoadingUtil.hideLoading(activity);
                        ToastUtil.toast("reqTag.tag = " + reqTag.getTag() + ", response.data.name = " + response.data.name);
                    }

                    @Override
                    public void onError(@NonNull ReqTag reqTag, @NonNull ErrorBean errorBean) {
                        LoadingUtil.hideLoading(activity);
                        ToastUtil.toast("reqTag.tag = " + reqTag.getTag() + ", errorBean.toString() = " + errorBean.toString());
                    }
                }
        );
    }

    public void jump2Mvp(View view) {
        startActivity(new Intent(this, MvpActivity.class));
    }

    public void switchApi(View view) {
        String api = switchApiEdit.getText().toString().trim();
        if (TextUtils.isEmpty(api)) {
            api = API.API_BASE;
        }
        RequestManager.get().switchApi(api);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
