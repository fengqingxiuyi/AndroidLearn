package com.example.utils.device

import android.hardware.Camera

/**
 * 判断摄像头是否可用
 */
object CameraUtil {

    @JvmStatic
    fun isCameraCanUse(): Boolean {
        var canUse = true
        var mCamera: Camera? = null
        try {
            mCamera = Camera.open()
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            val mParameters = mCamera.parameters
            mCamera.parameters = mParameters
        } catch (e: Exception) {
            canUse = false
        }
        mCamera?.release()
        return canUse
    }
}