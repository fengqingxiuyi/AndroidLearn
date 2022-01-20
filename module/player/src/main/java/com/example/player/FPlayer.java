package com.example.player;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import com.example.player.strategy.CacheDataSourceFactory;
import com.example.player.strategy.CustomLoadControl;
import com.example.log.LogUtil;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

/**
 * 基于ExoPlayer的播放器
 */
public class FPlayer {

    private static final String TAG = "FPlayer";

    private static final int HANDLER_TAG_POSITION = 1;

    /**
     * 成员变量
     **/
    //上下文
    private final Context context;
    //资源地址
    private final Uri uri;
    //内存缓存最大值，单位是字节
    private final long maxCacheSize;
    //文件缓存最大值，单位是字节
    private final long maxFileSize;
    //创建默认合适的带宽
    private DefaultBandwidthMeter bandwidthMeter;
    //
    private CustomLoadControl loadControl;
    //播放器
    private SimpleExoPlayer player;
    //进度监听
    private FPlayerHandler handler;
    //回调接口
    private IFPlayer iFPlayer;
    //资源播放进度
    private int currentPosition;
    //资源缓冲进度
    private int bufferedPosition;
    //资源总时长
    private int duration;

    private FPlayer(Builder builder) {
        this.context = builder.context;
        this.uri = builder.uri;
        this.maxCacheSize = builder.maxCacheSize;
        this.maxFileSize = builder.maxFileSize;
    }

    public void init() {
        init(null);
    }

    public void init(PlayerView playerView) {
        createPlayer();
        attachPlayerView(playerView);
        preparePlayer();
        listenPlayer();
        //
        if (handler == null) {
            handler = new FPlayerHandler();
        }
    }

    /**
     * 播放器创建
     */
    private void createPlayer() {
        if (context == null) {
            LogUtil.e(TAG, "context is null");
            return;
        }
        if (uri == null) {
            LogUtil.e(TAG, "uri is null");
            return;
        }
        /*
        RenderersFactory是用来生产Renderer数组的。
        Renderer翻译过来就是渲染器的意思，就是把音频、视频、文字源显示在画面上或输出成声音。
        音视频文件的录制都是二进制的，只是时间间隔非常短，所以输出时让人感觉是连续的，而Renderer的工作就是“快速、连续地输出这些文件”。
        为什么是数组呢，因为一个媒体文件可能有音频、视频等多个轨道，一个Render对应一个轨道；
         */
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        //创建默认合适的带宽
        bandwidthMeter = new DefaultBandwidthMeter();
        //创建跟踪的工厂
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        /*
        TrackSelector翻译过来就是轨道选择器的意思，它在这里的作用就是轨道选择，
        它有个方法selectTracks()，返回TrackSelection数组，TrackSelection就是对轨道进行解析的。
        一个媒体文件会包含多个轨道，音轨、视频轨、文字轨等，所以需要多个TrackSelection；
         */
        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        /*
        LoadControl主要是解析的一些进度控制、参数控制等，主要是记录一些position
         */
        loadControl = new CustomLoadControl(context);
        //创建player
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }

    /**
     * 将playerView绑定到player上
     */
    private void attachPlayerView(PlayerView playerView) {
        if (playerView != null && player != null) {
            playerView.setPlayer(player);
        } else {
            LogUtil.e(TAG, "playerView == null || player == null");
        }
    }

    /**
     * 资源准备
     */
    private void preparePlayer() {
        CacheDataSourceFactory dataSourceFactory = new CacheDataSourceFactory(context, bandwidthMeter, maxCacheSize, maxFileSize);
        //准备播放器的MediaSource
        player.prepare(buildMediaSource(uri, dataSourceFactory));
    }

    /**
     * 创建要播放的媒体的MediaSource
     */
    private MediaSource buildMediaSource(Uri uri, DataSource.Factory dataSourceFactory) {
        int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH: //用于DASH播放
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(dataSourceFactory), dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_SS: //用于平滑流播放
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(dataSourceFactory), dataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_HLS: //用于HLS播放
                return new HlsMediaSource.Factory(
                        new DefaultHlsDataSourceFactory(dataSourceFactory))
                        .createMediaSource(uri);
            case C.TYPE_OTHER: //用于MP3，M4A，WebM，MPEG-TS和AAC
            default:
                return new ExtractorMediaSource.Factory(
                        dataSourceFactory)
                        .createMediaSource(uri);

        }
    }

    /**
     * 播放器监听
     */
    private void listenPlayer() {
        //Add a listener to receive events from the player.
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                LogUtil.d(TAG, " onTimelineChanged", " timeline = ", timeline, " manifest = ", manifest, " reason = ", reason);
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                LogUtil.d(TAG, " onTracksChanged", " trackGroups = ", trackGroups, " trackSelections = ", trackSelections);
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                LogUtil.d(TAG, " onLoadingChanged", " isLoading = ", isLoading);
                try {
                    if (iFPlayer != null) {
                        iFPlayer.isLoading(isLoading);
                    }
                } catch (RemoteException e) {
                    LogUtil.e(e);
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                LogUtil.d(TAG, " onPlayerStateChanged", " playWhenReady = ", playWhenReady, " playbackState = ", playbackState);
                switch (playbackState) {
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_BUFFERING:

                        break;
                    case Player.STATE_READY:
                        duration = (int) player.getDuration();
                        if (loadControl != null) {
                            loadControl.updateMaxBufferUs(duration * 1000L);
                        }
                        LogUtil.d(TAG, " duration = ", duration);
                        try {
                            if (iFPlayer != null) {
                                iFPlayer.getDuration(duration);
                            }
                        } catch (RemoteException e) {
                            LogUtil.e(e);
                        }
                        if (handler != null) {
                            Message msg = Message.obtain();
                            msg.what = HANDLER_TAG_POSITION;
                            handler.sendMessage(msg);
                        }
                        break;
                    case Player.STATE_ENDED:

                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                LogUtil.d(TAG, " onRepeatModeChanged", " repeatMode = ", repeatMode);
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                LogUtil.d(TAG, " onShuffleModeEnabledChanged", " shuffleModeEnabled = ", shuffleModeEnabled);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                LogUtil.d(TAG, " onPlayerError", " error = ", LogUtil.readThrowable(error));
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                LogUtil.d(TAG, " onPositionDiscontinuity", " reason = ", reason);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                LogUtil.d(TAG, " onPlaybackParametersChanged", " playbackParameters = ", playbackParameters);
            }

            @Override
            public void onSeekProcessed() {
                LogUtil.d(TAG, " onSeekProcessed");
            }
        });
    }

    /**
     * 设置回调
     */
    public void setCallback(IFPlayer iFPlayer) {
        this.iFPlayer = iFPlayer;
    }

    /**
     * true 允许移动网络下缓冲 false 反之
     */
    public void setEnableBufferOnMobile(boolean enableBufferOnMobile) {
        if (loadControl != null) {
            loadControl.setEnableBufferOnMobile(enableBufferOnMobile);
        }
    }

    /**
     * 获取资源播放进度
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 获取资源缓冲进度
     */
    public int getBufferedPosition() {
        return bufferedPosition;
    }

    /**
     * 获取资源总时长
     */
    public int getDuration() {
        return duration;
    }

    /**
     * 是否正在播放
     *
     * @return true 正在播放 false 相反
     */
    public boolean isPlaying() {
        return player != null && player.getPlayWhenReady();
    }

    /**
     * 开始播放
     */
    public void play() {
        if (player != null && !isPlaying()) {
            player.setPlayWhenReady(true);
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (player != null && isPlaying()) {
            player.setPlayWhenReady(false);
        }
    }

    /**
     * 跳转到指定位置播放
     */
    public void seekTo(long positionMs) {
        if (player != null) {
            player.seekTo(positionMs);
        }
    }

    /**
     * 释放播放器资源
     */
    public void release() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        destroyHandler();
    }

    /**
     * 销毁Handler
     */
    private void destroyHandler() {
        if (handler != null) {
            handler.removeMessages(Player.STATE_READY);
            handler = null;
        }
    }

    public static class Builder {
        /*
        required
         */
        private final Context context;
        private final Uri uri;
        /*
        optional
         */
        private long maxCacheSize;
        private long maxFileSize;

        public Builder(Context context, Uri uri) {
            this.context = context;
            this.uri = uri;
        }

        /**
         * 返回Builder对象本身，链式调用
         */
        public Builder maxCacheSize(long maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        public Builder maxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
            return this;
        }

        /**
         * 通过Builder构建所需对象
         */
        public FPlayer build() {
            return new FPlayer(this);
        }

    }

    /**
     * 进度监听
     */
    private class FPlayerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (player == null) {
                destroyHandler();
                return;
            }
            if (handler == null || msg == null) {
                return;
            }
            switch (msg.what) {
                case HANDLER_TAG_POSITION:
                    currentPosition = (int) player.getCurrentPosition();
                    bufferedPosition = (int) player.getBufferedPosition();
                    LogUtil.d(TAG, " currentPosition = ", currentPosition, " bufferedPosition = ", bufferedPosition);
                    if (currentPosition < duration) {
                        try {
                            if (iFPlayer != null) {
                                iFPlayer.getPosition(currentPosition, bufferedPosition);
                            }
                        } catch (RemoteException e) {
                            LogUtil.e(e);
                        }
                        Message newMsg = Message.obtain();
                        newMsg.what = HANDLER_TAG_POSITION;
                        handler.sendMessageDelayed(newMsg, 1000);
                    } else {
                        player.seekTo(0);
                        player.setPlayWhenReady(false);
                        handler.removeMessages(HANDLER_TAG_POSITION);
                    }
                    break;
            }
        }
    }

}
