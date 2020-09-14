// IFPlayerBinder.aidl
package com.example.player;

// Declare any non-default types here with import statements
import com.example.player.IFPlayer;

interface IFPlayerBinder {

    //资源数据相关
    void setCallback(IFPlayer iFPlayer);
    void setEnableBufferOnMobile(boolean enableBufferOnMobile);
    int getCurrentPosition();
    int getBufferedPosition();
    int getDuration();
    //资源操作行为相关
    boolean isPlaying();
    void play();
    void pause();
    void seekTo(long positionMs);
    void release();

}
