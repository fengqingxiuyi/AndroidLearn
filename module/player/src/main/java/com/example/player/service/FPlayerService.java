package com.example.player.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.player.FPlayer;
import com.example.player.IFPlayer;
import com.example.player.IFPlayerBinder;
import com.example.utils.LogUtil;

/**
 * Service
 */
public class FPlayerService extends Service {

    private static final String TAG = "FPlayerService";

    private FPlayer fPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent == null) {
            LogUtil.e(TAG, "intent is null");
            return serviceBinder;
        }
        String url = intent.getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            LogUtil.e(TAG, "url is empty");
            return serviceBinder;
        }
        if (fPlayer == null) {
            Uri uri = Uri.parse(url);
            long maxCacheSize = intent.getLongExtra("maxCacheSize", 0);
            long maxFileSize = intent.getLongExtra("maxFileSize", 0);
            fPlayer = new FPlayer.Builder(getApplicationContext(), uri)
                    .maxCacheSize(maxCacheSize)
                    .maxFileSize(maxFileSize)
                    .build();
        }
        fPlayer.init();
        return serviceBinder;
    }

    @Override
    public void onDestroy() {
        if (fPlayer != null) {
            fPlayer.release();
        }
    }

    private IFPlayerBinder.Stub serviceBinder = new IFPlayerBinder.Stub() {

        @Override
        public void setCallback(IFPlayer iFPlayer) throws RemoteException {
            fPlayer.setCallback(iFPlayer);
        }

        @Override
        public void setEnableBufferOnMobile(boolean enableBufferOnMobile) throws RemoteException {
            fPlayer.setEnableBufferOnMobile(enableBufferOnMobile);
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return fPlayer.getCurrentPosition();
        }

        @Override
        public int getBufferedPosition() throws RemoteException {
            return fPlayer.getBufferedPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return fPlayer.getDuration();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return fPlayer.isPlaying();
        }

        @Override
        public void play() throws RemoteException {
            fPlayer.play();
        }

        @Override
        public void pause() throws RemoteException {
            fPlayer.pause();
        }

        @Override
        public void seekTo(long positionMs) throws RemoteException {
            fPlayer.seekTo(positionMs);
        }

        @Override
        public void release() throws RemoteException {
            fPlayer.release();
        }
    };

}
