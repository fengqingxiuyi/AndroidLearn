package com.example.learn.ui.player;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.SeekBar;

import com.example.common.base.BaseActivity;
import com.example.common.ui.loading.LoadingUtil;
import com.example.learn.R;
import com.example.player.FPlayerServiceHelper;
import com.example.player.IFPlayer;
import com.example.player.video.VideoActivity;
import com.example.ui.toast.ToastUtil;
import com.example.log.LogUtil;

import org.jetbrains.annotations.NotNull;

public class PlayerActivity extends BaseActivity {

    private static final int HANDLER_TAG_POSITION = 1;
    private static final int HANDLER_TAG_DURATION = 2;
    private static final int HANDLER_TAG_LOADING = 3;

    //资源地址
    private static final String URL = "http://music.163.com/song/media/outer/url?id=281951.mp3";

    //findView
    private SeekBar playSeekBar;
    //seekTo停止之后才设置进度值
    private boolean seekToStop = true;
    //
    private IFPlayerHandler iFPlayerHandler;

    /**
     * 横竖屏切换时保存必要信息
     */
    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        if (playSeekBar != null) {
            outState.putInt("progress", playSeekBar.getProgress());
            outState.putInt("bufferedProgress", playSeekBar.getSecondaryProgress());
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * 横竖屏切换时读取必要信息
     */
    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (playSeekBar != null) {
            playSeekBar.setProgress(savedInstanceState.getInt("progress"));
            playSeekBar.setSecondaryProgress(savedInstanceState.getInt("bufferedProgress"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //
        initSeekBar();
    }

    /**
     * findView
     */
    private void initSeekBar() {
        playSeekBar = (SeekBar) findViewById(R.id.play_seek_bar);
        //初始化数值
        playSeekBar.setMax(FPlayerServiceHelper.get().getDuration());
        playSeekBar.setProgress(FPlayerServiceHelper.get().getCurrentPosition());
        playSeekBar.setSecondaryProgress(FPlayerServiceHelper.get().getBufferedPosition());
        //监听进度条进度的变化
        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekToStop = false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekToStop = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekToStop = true;
                FPlayerServiceHelper.get().seekTo(seekBar.getProgress());
            }
        });
    }

    /**
     * 初始化FPlayer
     */
    private void initFPlayer() {
        FPlayerServiceHelper.get()
                .setUrl(URL)
                .setMaxCacheSize(100 * 1024 * 1024)
                .setMaxFileSize(20 * 1024 * 1024)
                .setCallback(
                        new IFPlayer.Stub() { //回调都是在子线程中工作的

                            @Override
                            public void getPosition(int currentPosition, int bufferedPosition) throws RemoteException {
                                LogUtil.d("IFPlayer.Stub currentPosition = ", currentPosition, " bufferedPosition = ", bufferedPosition);
                                if (iFPlayerHandler != null) {
                                    Message msg = Message.obtain();
                                    msg.what = HANDLER_TAG_POSITION;
                                    msg.arg1 = currentPosition;
                                    msg.arg2 = bufferedPosition;
                                    iFPlayerHandler.sendMessage(msg);
                                }
                            }

                            @Override
                            public void getDuration(int duration) throws RemoteException {
                                LogUtil.d("IFPlayer.Stub duration = ", duration);
                                if (iFPlayerHandler != null) {
                                    Message msg = Message.obtain();
                                    msg.what = HANDLER_TAG_DURATION;
                                    msg.arg1 = duration;
                                    iFPlayerHandler.sendMessage(msg);
                                }
                            }

                            @Override
                            public void isLoading(boolean isLoading) throws RemoteException {
                                LogUtil.d("IFPlayer.Stub isLoading = ", isLoading);
                                if (iFPlayerHandler != null) {
                                    Message msg = Message.obtain();
                                    msg.what = HANDLER_TAG_LOADING;
                                    msg.obj = isLoading;
                                    iFPlayerHandler.sendMessage(msg);
                                }
                            }
                        }
                )
                .startService(this)
                .bindService(this);
    }

    /**
     * 音频播放初始化
     */
    public void init(View view) {
        if (iFPlayerHandler == null) {
            iFPlayerHandler = new IFPlayerHandler();
            initFPlayer();
        }
    }

    /**
     * 点击开始播放
     */
    public void play(View view) {
        FPlayerServiceHelper.get().setEnableBufferOnMobile(true);
        FPlayerServiceHelper.get().play();
    }

    /**
     * 点击暂停播放
     */
    public void pause(View view) {
        FPlayerServiceHelper.get().pause();
    }

    /**
     * 跳转到视频播放页面
     */
    public void jump2Video(View view) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("url", "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iFPlayerHandler != null) {
            iFPlayerHandler.removeCallbacksAndMessages(null);
            iFPlayerHandler = null;
        }
        FPlayerServiceHelper.get().unbindService(this);
    }

    private class IFPlayerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_TAG_POSITION:
                    if (seekToStop) {
                        //设置数值
                        playSeekBar.setProgress(msg.arg1);
                        playSeekBar.setSecondaryProgress(msg.arg2);
                    }
                    break;
                case HANDLER_TAG_DURATION:
                    playSeekBar.setMax(msg.arg1);
                    break;
                case HANDLER_TAG_LOADING://暂时注释
                    if ((Boolean) msg.obj) {
                        LoadingUtil.showLoading(activity);
                    } else {
                        LoadingUtil.hideLoading(activity);
                        ToastUtil.toast("音频播放初始化成功");
                    }
                    break;
            }
        }
    }
}
