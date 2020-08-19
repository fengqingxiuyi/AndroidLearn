package com.example.learn.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.download.bean.FileInfo;
import com.example.download.bean.ThreadInfo;
import com.example.download.service.DownloadService;
import com.example.learn.R;

/**
 * @author fqxyi
 * @date 2020/8/19
 */
public class DownloadActivity extends AppCompatActivity {

    private TextView tvFileName;
    private ProgressBar pbProgress;
    private Button btStop;
    private Button btStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        //初始化组件
        tvFileName = (TextView) findViewById(R.id.tvFileName);
        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        btStop = (Button) findViewById(R.id.btStop);
        btStart = (Button) findViewById(R.id.btStart);
        //
        tvFileName.setText("mukewang.apk");
        pbProgress.setMax(100);
        //创建文件信息对象
        final FileInfo fileInfo = new FileInfo(
                0,
                "http://www.imooc.com/mobile/mukewang.apk",
                "mukewang.apk",
                0,
                0
        );
        //添加事件监听
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过intent传递参数给service
                Intent intent = new Intent(DownloadActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra(FileInfo.KEY, fileInfo);
                startService(intent);
            }
        });
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过intent传递参数给service
                Intent intent = new Intent(DownloadActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra(FileInfo.KEY, fileInfo);
                startService(intent);
            }
        });
        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        registerReceiver(receiver, filter);
    }

    /**
     * 更新UI的广播接收器
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra(ThreadInfo.KEY_FINISHED, 0);
                pbProgress.setProgress(finished);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
