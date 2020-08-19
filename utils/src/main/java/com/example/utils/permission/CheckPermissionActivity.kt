package com.example.utils.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.SparseArray
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

/**
 * @author fqxyi
 * @date 2020/8/19
 * 权限检查基类
 */
open class CheckPermissionActivity : AppCompatActivity() {

    private val permissionCallbackList: SparseArray<PermissionCallback> by lazy {
        SparseArray<PermissionCallback>()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionCallback = permissionCallbackList[requestCode] ?: return
        if (grantResults.isEmpty() || permissions.isEmpty()) {
            return
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionCallback.permissionGranted()
            permissionCallbackList.remove(requestCode)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                if (TextUtils.isEmpty(permissionCallback.getPermissionDesc())) {
                    return
                }
                AlertDialog.Builder(this)
                    .setMessage(permissionCallback.getPermissionDesc())
                    .setNegativeButton("拒绝") { dialog, _ ->
                        permissionCallback.permissionDenied()
                        dialog.dismiss()
                    }
                    .setNeutralButton("允许") { dialog, _ ->
                        ActivityCompat.requestPermissions(this, permissions, requestCode)
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                permissionCallbackList.remove(requestCode)
                if (TextUtils.isEmpty(permissionCallback.getPermissionDeniedMsg())) {
                    return
                }
                AlertDialog.Builder(this)
                    .setMessage(permissionCallback.getPermissionDeniedMsg())
                    .setNegativeButton("拒绝") { dialog, _ ->
                        permissionCallback.permissionDenied()
                        dialog.dismiss()
                    }
                    .setNeutralButton("去设置") { dialog, _ ->
                        permissionCallback.permissionDenied()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:$packageName")
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    open fun requestPermission(permission: String, permissionCallback: PermissionCallback?) {
        if (null == permissionCallback) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    application,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                permissionCallback.permissionGranted()
            } else {
                val calendar = Calendar.getInstance()
                val code = calendar[Calendar.MINUTE] * 60 + calendar[Calendar.SECOND]
                permissionCallbackList.put(code, permissionCallback)
                ActivityCompat.requestPermissions(this, arrayOf(permission), code)
            }
        } else {
            permissionCallback.permissionGranted()
        }
    }

    interface PermissionCallback {
        //用户允许权限
        fun permissionGranted()

        //用户拒绝权限
        fun permissionDenied()

        //用户曾经拒绝权限，并点击了不再询问。获取此场景下的提示文案
        fun getPermissionDeniedMsg(): String

        //向用户表明申请该权限的目的
        fun getPermissionDesc(): String
    }

}