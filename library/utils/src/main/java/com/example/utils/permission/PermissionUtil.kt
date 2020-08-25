package com.example.utils.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

/**
 * 权限工具类
 */
object PermissionUtil {
    /**
     * REQUEST READ_PHONE_STATE
     */
    @JvmStatic
    fun requestPhonePermission(
        activity: CheckPermissionActivity,
        permissionCallback: PermissionCallback?
    ) {
        activity.requestPermission(
            Manifest.permission.READ_PHONE_STATE,
            object :
                CheckPermissionActivity.PermissionCallback {
                override fun permissionGranted() {
                    permissionCallback?.permissionGranted()
                }

                override fun permissionDenied() {
                    permissionCallback?.permissionDenied()
                }

                override fun getPermissionDeniedMsg(): String {
                    return "请手动去设置页面开启设备权限"
                }

                override fun getPermissionDesc(): String {
                    return "应用需要设备权限去获取本机识别码等信息"
                }
            })
    }

    @JvmStatic
    fun hasPhonePermission(context: Context): Boolean {
        return context.checkCallingOrSelfPermission(
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * REQUEST ACCESS_FINE_LOCATION
     */
    @JvmStatic
    fun requestLocationPermission(
        activity: CheckPermissionActivity,
        permissionCallback: PermissionCallback?
    ) {
        activity.requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            object :
                CheckPermissionActivity.PermissionCallback {
                override fun permissionGranted() {
                    permissionCallback?.permissionGranted()
                }

                override fun permissionDenied() {
                    permissionCallback?.permissionDenied()
                }

                override fun getPermissionDeniedMsg(): String {
                    return "请手动去设置页面开启定位权限"
                }

                override fun getPermissionDesc(): String {
                    return "应用需要定位权限去获取您的当前位置"
                }
            })
    }

    @JvmStatic
    fun hasLocationPermission(context: Context): Boolean {
        return context.checkCallingOrSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * REQUEST WRITE_EXTERNAL_STORAGE
     */
    @JvmStatic
    fun requestStoragePermission(
        activity: CheckPermissionActivity,
        permissionCallback: PermissionCallback?
    ) {
        activity.requestPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            object :
                CheckPermissionActivity.PermissionCallback {
                override fun permissionGranted() {
                    permissionCallback?.permissionGranted()
                }

                override fun permissionDenied() {
                    permissionCallback?.permissionDenied()
                }

                override fun getPermissionDeniedMsg(): String {
                    return "请手动去设置页面开启存储权限"
                }

                override fun getPermissionDesc(): String {
                    return "应用需要存储权限去保存图片和下载安装包"
                }
            })
    }

    @JvmStatic
    fun hasStoragePermission(context: Context): Boolean {
        return context.checkCallingOrSelfPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * REQUEST CAMERA
     */
    @JvmStatic
    fun requestCameraPermission(
        activity: CheckPermissionActivity,
        permissionCallback: PermissionCallback?
    ) {
        activity.requestPermission(
            Manifest.permission.CAMERA,
            object :
                CheckPermissionActivity.PermissionCallback {
                override fun permissionGranted() {
                    permissionCallback?.permissionGranted()
                }

                override fun permissionDenied() {
                    permissionCallback?.permissionDenied()
                }

                override fun getPermissionDeniedMsg(): String {
                    return "请手动去设置页面开启相机权限"
                }

                override fun getPermissionDesc(): String {
                    return "应用需要相机权限去扫一扫或拍照"
                }
            })
    }

    @JvmStatic
    fun hasCameraPermission(context: Context): Boolean {
        return context.checkCallingOrSelfPermission(
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    interface PermissionCallback {
        fun permissionGranted() // 用户允许权限
        fun permissionDenied() //用户拒绝权限
    }
}