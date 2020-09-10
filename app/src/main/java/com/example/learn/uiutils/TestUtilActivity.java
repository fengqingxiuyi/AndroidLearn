package com.example.learn.uiutils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.common.base.BaseActivity;
import com.example.common.ui.dialog.DialogMessageFragment;
import com.example.common.ui.titlebar.TitleBarView;
import com.example.learn.R;
import com.example.learn.uiutils.event.TestRxBusCancelEvent;
import com.example.learn.uiutils.event.TestRxBusSendEvent;
import com.example.ui.toast.ToastUtil;
import com.example.utils.LogUtil;
import com.example.utils.device.CameraUtil;
import com.example.utils.device.InputMethodUtil;
import com.example.utils.download.DownloadUtil;
import com.example.utils.event.RxBusUtil;
import com.example.utils.format.CalculateAgeUtil;
import com.example.utils.format.NumFormat;
import com.example.utils.network.NetworkUtil;
import com.example.utils.permission.PermissionUtil;
import com.example.utils.storage.FileUtil;
import com.example.utils.storage.StorageManagerUtil;
import com.example.utils.view.TextViewUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试Util组件的主要类
 */
public class TestUtilActivity extends BaseActivity {

    private TitleBarView testUtilTitleBar;

    private DialogMessageFragment dialogMessageFragment;

    private DownloadUtil downloadUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_util);

        testUtilTitleBar = (TitleBarView) findViewById(R.id.test_util_title_bar);

    }

    /**
     * 切换软键盘的显示和隐藏
     */
    public void testEditToggle(View view) {
        EditText testEdit = (EditText) findViewById(R.id.test_edit);
        Button testEditToggleBtn = (Button) findViewById(R.id.test_edit_toggle_btn);
        if (InputMethodUtil.isShowing(this)) {
            InputMethodUtil.hideInput(testEdit);
            testEditToggleBtn.setText("唤醒软键盘");
        } else {
            InputMethodUtil.showInput(testEdit);
            testEditToggleBtn.setText("隐藏软键盘");
        }
    }

    /**
     * 测试数据填充
     */
    public void testDataFill(View view) {
        TextView testFillData = (TextView) findViewById(R.id.test_fill_data);
        if ("测试数据填充占位".equals(testFillData.getText().toString().trim())) {
            ((Button) view).setText("清除");
            TextViewUtil.dataFill(testFillData, "%s考了%d分", "小明", 88);
        } else {
            ((Button) view).setText("生成");
            testFillData.setText("测试数据填充占位");
        }
    }

    /**
     * 测试缓存清理
     */
    public void testClearCache(View view) {
        TextView testCacheSize = (TextView) findViewById(R.id.test_cache_size);
        Button testCacheClear = (Button) findViewById(R.id.test_cache_clear);
        if (testCacheClear.getText().equals("显示缓存")) {
            testCacheClear.setText("清除缓存");
            testCacheSize.setText("应用缓存：" + FileUtil.getTotalCacheSize(context));
        } else {
            testCacheClear.setText("显示缓存");
            FileUtil.clearAllCache(context);
        }
    }

    /**
     * 测试SharedPreferences
     */
    public void testSP(View view) {
        TextView testSpText = (TextView) findViewById(R.id.test_sp_text);
        Button testSpBtn = (Button) findViewById(R.id.test_sp_btn);
        if (testSpBtn.getText().equals("写入文件")) {
            StorageManagerUtil.putToDisk("TEST", "test", "test2test");
            testSpBtn.setText("读取文件");
            testSpText.setText("写入成功");
        } else {
            testSpBtn.setText("写入文件");
            testSpText.setText("文件内容：" + StorageManagerUtil.get("TEST", "test", null));
        }
    }

    /**
     * 测试网络状态
     */
    public void testNetwork(View view) {
        String content = "网络是否连接: " + NetworkUtil.isNetworkAvailable(context) + "\n" +
                "网络连接是否可用: " + NetworkUtil.isNetworkConnected(context) + "\n" +
                "是否已连接Wifi: " + NetworkUtil.isWifiConnected(context) + "\n" +
                "是否已连接移动网络: " + NetworkUtil.isMobileConnected(context) + "\n" +
                "当前网络类型名: " + NetworkUtil.getConnectedTypeName(context) + "\n" +
                "连接Wifi时的ip地址: " + NetworkUtil.getWifiIpAddress(context) + "\n" +
                "连接移动网络时的ip地址: " + NetworkUtil.getMobileIpAddress() + "\n" +
                "Mac地址，不准确: " + NetworkUtil.getMacAddress(context) + "\n";

        if (null == dialogMessageFragment) {
            dialogMessageFragment = new DialogMessageFragment();
        }
        dialogMessageFragment.setTitle("网络状态")
                .setContent(content)
                .setRightText("确认")
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogMessageFragment.dismissAllowingStateLoss();
                    }
                })
                .show(activity);
    }

    /**
     * 测试多文件下载（支持单文件下载）
     * 需要判断是否添加网络权限和写入外部存储卡的权限以及是否联网
     */
    public void testFilesDownload(View view) {
        if (NetworkUtil.isNetworkAvailable(context)) {
            PermissionUtil.requestStoragePermission(this, new PermissionUtil.PermissionCallback() {
                @Override
                public void permissionGranted() {
                    downloadFiles();
                    downloadFiles2();
                }

                @Override
                public void permissionDenied() {

                }
            });
        } else {
            toast(getResources().getString(R.string.network_unavailable));
        }
    }

    /**
     * 测试RxBus事件的发送与取消
     */
    public void testRxBus(View view) {
        Button testRxBusBtn = (Button) findViewById(R.id.test_rx_bus_btn);
        if (testRxBusBtn.getText().equals("发送RxBus事件")) {
            testRxBusBtn.setText("取消事件订阅");
            RxBusUtil.getInstance().post(new TestRxBusSendEvent("已经在MainActivity中订阅该事件，所以我才出现"));
        } else {
            testRxBusBtn.setText("发送RxBus事件");
            RxBusUtil.getInstance().post(new TestRxBusCancelEvent());
        }
    }

    /**
     * 测试 下载url集合
     */
    private void downloadFiles() {
        List<String> list = new ArrayList<>();
        list.add("http://img1d.xgo-img.com.cn/pics/1545/1544725.jpg");
        list.add("http://img1a.xgo-img.com.cn/pics/1545/1544726.jpg");
        list.add("http://img1b.xgo-img.com.cn/pics/1545/1544727.jpg");
        list.add("http://dzs.qisuu.com/txt/盖世神操.txt");
        list.add("http://dzs.qisuu.com/txt/%E7%9B%96%E4%B8%96%E7%A5%9E%E6%93%8D.txt");
        new DownloadUtil(context, FileUtil.getDefaultFilePath(context), list, new DownloadUtil.DownloadStateListener() {
            @Override
            public void onStart() {
                toast("开始下载文件");
            }

            @Override
            public void onFailure(List<Integer> failureIndexs) {
                toast("下载失败");
            }

            @Override
            public void onAllSucceed(String fileDownloadDir) {
                toast("下载成功，下载位置：" + fileDownloadDir);
            }

            @Override
            public void onError(IOException e) {
                toast("下载错误, 错误信息：" + e.getMessage());
            }
        }).startDownload();
    }

    /**
     * 测试 循环下载单个url
     */
    private void downloadFiles2() {
        List<String> list = new ArrayList<>();
        list.add("http://img1d.xgo-img.com.cn/pics/1545/1544725.jpg");
        list.add("http://img1a.xgo-img.com.cn/pics/1545/1544726.jpg");
        list.add("http://img1b.xgo-img.com.cn/pics/1545/1544727.jpg");
        list.add("http://dzs.qisuu.com/txt/盖世神操.txt");
        list.add("http://dzs.qisuu.com/txt/%E7%9B%96%E4%B8%96%E7%A5%9E%E6%93%8D.txt");
        for (int i = 0; i < 5; i++) {
            if (downloadUtil == null) {
                downloadUtil = new DownloadUtil(context, FileUtil.getDefaultFilePath(context), list.get(i), new DownloadUtil.DownloadStateListener() {
                    @Override
                    public void onStart() {
                        toast("开始下载文件");
                    }

                    @Override
                    public void onFailure(List<Integer> failureIndexs) {
                        toast("下载失败");
                    }

                    @Override
                    public void onAllSucceed(String fileDownloadDir) {
                        toast("下载成功，下载位置：" + fileDownloadDir);
                    }

                    @Override
                    public void onError(IOException e) {
                        toast("下载错误, 错误信息：" + e.getMessage());
                    }
                });
            } else {
                downloadUtil.update(list.get(i));
            }
            downloadUtil.startDownload();
        }
    }

    /**
     * 申请相机权限
     */
    public void testCameraRequest(View view) {
        PermissionUtil.requestCameraPermission(this, new PermissionUtil.PermissionCallback() {
            @Override
            public void permissionGranted() {
                toast("已获取到相机权限");
            }

            @Override
            public void permissionDenied() {

            }
        });
    }

    /**
     * 测试摄像头权限
     */
    public void testCamera(View view) {
        toast("判断摄像头是否可用：" + CameraUtil.isCameraCanUse());
    }

    /**
     * 计算年龄，完整的年龄格式为：xx岁xx个月xx天
     */
    public void testCalculateAge(View view) {
        String age = CalculateAgeUtil.calculate(CalculateAgeUtil.getDate("2016-01-30"), new Date());
        ToastUtil.toast(age);
    }

    /**
     * 测试数字格式化
     */
    public void testNumFormat(View view) {
        LogUtil.d(NumFormat.get().formatCeiling("#.##", 9.4f));
        LogUtil.d(NumFormat.get().formatCeiling("#.##", 9.5f));
        LogUtil.d(NumFormat.get().formatCeiling("#.##", 9.48f));
        LogUtil.d(NumFormat.get().formatCeiling("#.##", 9.485f));
        LogUtil.d(NumFormat.get().formatCeiling("#.##", 9.486f));
        LogUtil.d(NumFormat.get().formatCeiling("0.00", 9.4f));
        LogUtil.d(NumFormat.get().formatCeiling("0.00", 9.5f));
        LogUtil.d(NumFormat.get().formatCeiling("0.00", 9.48f));
        LogUtil.d(NumFormat.get().formatCeiling("0.00", 9.485f));
        LogUtil.d(NumFormat.get().formatCeiling("0.00", 9.486f));
    }

    @Override
    public TitleBarView getTitleBarView() {
        return testUtilTitleBar;
    }

}
