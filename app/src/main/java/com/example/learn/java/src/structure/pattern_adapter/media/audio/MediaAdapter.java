package com.example.learn.java.src.structure.pattern_adapter.media.audio;

import com.example.learn.java.src.structure.pattern_adapter.media.advanced.IAdvancedMediaPlayer;
import com.example.learn.java.src.structure.pattern_adapter.media.advanced.impl.Mp4Player;
import com.example.learn.java.src.structure.pattern_adapter.media.advanced.impl.VlcPlayer;

/**
 * @author fqxyi
 * @desc 实现了 IMediaPlayer 接口的适配器类。
 * @date 2018/7/24
 */
public class MediaAdapter implements IMediaPlayer {

    IAdvancedMediaPlayer advancedMusicPlayer;

    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedMusicPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedMusicPlayer.playMp4(fileName);
        }
    }

}
