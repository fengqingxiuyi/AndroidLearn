package com.example.player.video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.player.FPlayer;
import com.example.player.R;
import com.example.log.LogUtil;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoActivity extends Activity {

    private static final String TAG = "VideoActivity";

    //findView
    private PlayerView playerView;
    //
    private FPlayer fPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        //findView
        playerView = (PlayerView) findViewById(R.id.player_view);
        initFPlayer();
    }

    /**
     * 初始化FPlayer
     */
    private void initFPlayer() {
        Intent intent = getIntent();
        if (intent == null) {
            LogUtil.e(TAG, "intent is null");
            return;
        }
        String url = intent.getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            LogUtil.e(TAG, "url is empty");
            return;
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
        fPlayer.init(playerView);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        // See whether the player view wants to handle media or DPAD keys events.
//        return playerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (playerView != null && playerView.getOverlayFrameLayout() != null) {
//            playerView.getOverlayFrameLayout().removeAllViews();
//        }
        if (fPlayer != null) {
            fPlayer.release();
            fPlayer = null;
        }
    }
}
