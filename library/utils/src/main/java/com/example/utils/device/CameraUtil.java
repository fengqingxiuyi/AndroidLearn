package com.example.utils.device;

import android.hardware.Camera;

/**
 * 判断摄像头是否可用
 */
public class CameraUtil {

    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }

}
