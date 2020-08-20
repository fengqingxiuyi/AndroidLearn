package com.example.shake;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ui.DialogBaseFragment;
import com.example.utils.ClipboardUtil;
import com.example.utils.DeviceUtil;
import com.example.utils.permission.CheckPermissionActivity;
import com.example.utils.permission.PermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author fqxyi
 * @date 2017/1/18
 */
public class ShakeSensorDialog extends DialogBaseFragment {

    private static final int WEBHOOK_TOKEN = 0;

    private TextView viewShakeTip;
    private EditText viewShakeEdit;
    private LinearLayout viewShakeCheckContainer;
    private CheckBox viewShakeCheck;
    private Button viewShakeCancel;
    private Button viewShakeConfirm;

    private String url;
    private String filePath;

    private String tip;
    private boolean editVisible = true;
    private boolean checkVisible = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WEBHOOK_TOKEN) {
                String responseUrl = (String) msg.obj;

                ClipboardUtil.copy(context, responseUrl);
                toast("文件上传成功, 文件链接已复制到剪切板，可直接访问");

                ShakeSensorUtil.getInstance().postUploadJson(
                        ShakeSensorConstant.WEBHOOK_TOKEN_UPLOAD_IMAGE,
                        "{'msgtype':'text',"
                                + "'text': {"
                                + "'content':'" + "已上传图片的响应地址：" +responseUrl + "'"
                                + "}"
                                + "}",
                        new UploadCallback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                toast("文件上传通知到机器人失败");
                            }

                            /**
                             * respone : {"errmsg":"ok","errcode":0}
                             */
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String jsonData = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonData);
                                    int errcode = jsonObject.getInt("errcode");
                                    if (0 != errcode) {
                                        toast("文件上传失败");
                                    } else {
                                        toast("文件上传通知到机器人 is " + jsonObject.getString("errmsg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setSize((int) (DeviceUtil.getScreenWidth(context) * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getContentView() {
        return R.layout.view_shake_sensor;
    }

    @Override
    public void initView(View view) {

        setBottom(false);

        viewShakeTip = (TextView) findViewById(R.id.view_shake_tip);
        viewShakeEdit = (EditText) findViewById(R.id.view_shake_edit);
        viewShakeCheckContainer = (LinearLayout) findViewById(R.id.view_shake_check_container);
        viewShakeCheck = (CheckBox) findViewById(R.id.view_shake_check);
        viewShakeCancel = (Button) findViewById(R.id.view_shake_cancel);
        viewShakeConfirm = (Button) findViewById(R.id.view_shake_confirm);

        viewShakeTip.setText(tip);
        if (editVisible) {
            viewShakeEdit.setVisibility(View.VISIBLE);
        } else {
            viewShakeEdit.setVisibility(View.GONE);
        }
        if (checkVisible) {
            viewShakeCheckContainer.setVisibility(View.VISIBLE);
        } else {
            viewShakeCheckContainer.setVisibility(View.GONE);
        }

        initEvent();

    }

    private void initEvent() {

        viewShakeCheckContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewShakeCheck.isChecked()) {
                    viewShakeCheck.setChecked(false);
                    // 不上传屏幕截图 状态
                } else {
                    viewShakeCheck.setChecked(true);
                    // 上传屏幕截图 状态
                }
            }
        });

        viewShakeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭弹框
                dismissAllowingStateLoss();
            }
        });

        viewShakeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtil.requestStoragePermission((CheckPermissionActivity) activity, new PermissionUtil.PermissionCallback() {
                    @Override
                    public void permissionGranted() {
                        if (View.VISIBLE == viewShakeEdit.getVisibility()) {
                            String content = viewShakeEdit.getText().toString().trim();
                            if (!TextUtils.isEmpty(content)) {
                                // 获取反馈内容
                            } else {
                                toast("请填写反馈内容");
                            }
                        }

                        // 发送数据到服务器
                        if (View.VISIBLE == viewShakeCheck.getVisibility() && viewShakeCheck.isChecked()) {
                            // 上传屏幕截图 内容
                            ShakeSensorUtil.getInstance().postUploadImage(url, filePath, new UploadCallback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    toast("文件上传失败");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String jsonData = response.body().string();
                                    try {
                                        JSONObject jsonObject = new JSONObject(jsonData);

                                        if (0 == jsonObject.getInt("isSuccess")) {
                                            toast("文件上传失败");
                                            return;
                                        }

                                        Message msg = Message.obtain();
                                        msg.what = WEBHOOK_TOKEN;
                                        msg.obj = jsonObject.getString("data");
                                        handler.sendMessage(msg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void permissionDenied() {

                    }
                });
                // 关闭弹框
                dismissAllowingStateLoss();
            }
        });
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setEditVisible(boolean editVisible) {
        this.editVisible = editVisible;
    }

    public void setCheckVisible(boolean checkVisible) {
        this.checkVisible = checkVisible;
    }

    public void setUrlAndFilePath(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }
    
    private void toast(final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != handler) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
