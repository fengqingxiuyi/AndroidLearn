package com.example.shake;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author fqxyi
 * @date 2017/1/18
 */
public class ShakeSensorManager {

    private static final int SHOW_DIALOG = 0;

    private FragmentActivity activity;

    private String filePath = null;

    private OnShakeListener onShakeListener;

    private ShakeSensorDialog shakeSensorDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (SHOW_DIALOG == msg.what) {
                if (null == activity) {
                    return;
                }
                // 截屏
                boolean success = screenshot();

                if (shakeSensorDialog == null) {
                    shakeSensorDialog = new ShakeSensorDialog();
                    shakeSensorDialog.setEditVisible(false);
                }
                // 显示提示
                if (success) {
                    shakeSensorDialog.setTip("保存截屏成功，位置：" + filePath);
                } else {
                    shakeSensorDialog.setTip("保存截屏失败，请确认是否已开启存储权限");
                }
                shakeSensorDialog.setUrlAndFilePath(ShakeSensorConstant.URL, filePath);

                shakeSensorDialog.show(activity);
            }
        }
    };

    private ShakeSensor shakeSensor = null;

    private static ShakeSensorManager sShakeSensorManager = new ShakeSensorManager();

    private ShakeSensorManager() {

    }

    public static ShakeSensorManager getInstance() {
        return sShakeSensorManager;
    }

    public void onActivityResumed(FragmentActivity activity) {
        onActivityResumed(activity, ShakeSensor.mSpeedThreshold);
    }

    public void onActivityResumed(FragmentActivity activity, int speedThreshold) {
        if (null == shakeSensor) {
            shakeSensor = new ShakeSensor(activity.getApplicationContext(), speedThreshold);
            onShakeListener = new OnShakeListener() {
                @Override
                public void onShakeComplete() {
                    handler.sendEmptyMessage(SHOW_DIALOG);
                }
            };
        }
        this.activity = activity;
        shakeSensor.setShakeListener(onShakeListener);
        shakeSensor.register();
    }

    public void onActivityPaused() {
        this.activity = null;
        shakeSensor.unregister();
    }

    private boolean screenshot() {
        if (null == activity) {
            return false;
        }
        try {
            // 获取屏幕
            View dView = activity.getWindow().getDecorView();
            dView.setDrawingCacheEnabled(true);
            dView.buildDrawingCache();
            Bitmap bmp = dView.getDrawingCache();
            if (bmp == null) {
                return false;
            }
            // 获取内置SD卡路径
            String parentPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "Shake" + File.separator + "feedback";
            File parentDir = new File(parentPath);
            // 创建目录
            if (!parentDir.exists()) parentDir.mkdirs();
            // 图片文件路径
            filePath = parentDir + File.separator + System.currentTimeMillis() + ".png";
            // 保存截屏
            FileOutputStream os = new FileOutputStream(new File(filePath));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getFilePath() {
        return filePath;
    }

}
