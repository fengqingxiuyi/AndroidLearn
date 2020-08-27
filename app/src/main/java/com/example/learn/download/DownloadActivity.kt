package com.example.learn.download

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.download.bean.FileInfo
import com.example.download.bean.ThreadInfo
import com.example.download.service.DownloadService
import com.example.learn.R
import com.example.utils.permission.PermissionUtil
import com.example.utils.permission.PermissionUtil.requestStoragePermission
import kotlinx.android.synthetic.main.activity_download.*

/**
 * @author fqxyi
 * @date 2020/8/19
 */
class DownloadActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
        testDownload()
    }

    private fun testDownload() {
        tvFileName.text = "mukewang.apk"
        pbProgress.max = 100
        //创建文件信息对象
        val fileInfo = FileInfo(
            0,
            "http://www.imooc.com/mobile/mukewang.apk",
            "mukewang.apk",
            0,
            0
        )
        //添加事件监听
        btStart.setOnClickListener {
            requestStoragePermission(
                activity,
                object : PermissionUtil.PermissionCallback {
                    override fun permissionGranted() {
                        //通过intent传递参数给service
                        val intent = Intent(this@DownloadActivity, DownloadService::class.java)
                        intent.action = DownloadService.ACTION_START
                        intent.putExtra(FileInfo.KEY, fileInfo)
                        startService(intent)
                    }

                    override fun permissionDenied() {}
                })
        }
        btStop.setOnClickListener {
            requestStoragePermission(
                activity,
                object : PermissionUtil.PermissionCallback {
                    override fun permissionGranted() {
                        //通过intent传递参数给service
                        val intent = Intent(this@DownloadActivity, DownloadService::class.java)
                        intent.action = DownloadService.ACTION_STOP
                        intent.putExtra(FileInfo.KEY, fileInfo)
                        startService(intent)
                    }

                    override fun permissionDenied() {}
                })
        }
        //注册广播接收器
        val filter = IntentFilter()
        filter.addAction(DownloadService.ACTION_UPDATE)
        registerReceiver(receiver, filter)
    }

    /**
     * 更新UI的广播接收器
     */
    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (DownloadService.ACTION_UPDATE == intent.action) {
                val finished = intent.getIntExtra(ThreadInfo.KEY_FINISHED, 0)
                pbProgress.progress = finished
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}