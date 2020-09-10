package com.example.learn.uiutils;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.common.ui.titlebar.TitleBarView;
import com.example.learn.R;
import com.example.utils.device.IntentUtil;

/**
 * IntentUtil测试类
 */
public class TestIntentActivity extends BaseActivity {

    private TitleBarView testIntentTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_intent);

        testIntentTitleBar = (TitleBarView) findViewById(R.id.test_intent_title_bar);
    }

    public void setting(View view) {
        startActivity(IntentUtil.getSettingIntent());
    }

    public void wifiSetting(View view) {
        startActivity(IntentUtil.getWifiSettingIntent());
    }

    public void deviceInfoSetting(View view) {
        startActivity(IntentUtil.getDeviceInfoSettingIntent());
    }

    public void appDevelopSetting(View view) {
        startActivity(IntentUtil.getAppDevelopSettingIntent());
    }

    public void appSetting(View view) {
        startActivity(IntentUtil.getAppSettingIntent());
    }

    public void appDetailSetting(View view) {
        startActivity(IntentUtil.getAppDetailSettingIntent(context));
    }

    public void showTel(View view) {
        startActivity(IntentUtil.getShowTelIntent("10010"));
    }

    public void showSMS(View view) {
        startActivity(IntentUtil.getShowSMSIntent("10010"));
    }

    public void showCamera(View view) {
        sendBroadcast(IntentUtil.getShowCameraIntent());
    }

    public void showGallery(View view) {
        startActivity(IntentUtil.getShowGalleryIntent("image/*"));
    }

    public void showRecorder(View view) {
        startActivity(IntentUtil.getShowRecorderIntent());
    }

    public void callSearch(View view) {
        startActivity(IntentUtil.getCallSearchIntent("想搜索的内容"));
    }

    public void callWeb(View view) {
        startActivity(IntentUtil.getCallWebIntent("http://wwww.baidu.com/"));
    }

    public void callShare(View view) {
        startActivity(IntentUtil.getCallShareIntent("我是标题", "我是内容"));
    }

    @Override
    public TitleBarView getTitleBarView() {
        return testIntentTitleBar;
    }

}
