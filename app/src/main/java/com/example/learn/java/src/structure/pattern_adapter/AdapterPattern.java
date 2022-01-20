package com.example.learn.java.src.structure.pattern_adapter;

import com.example.learn.java.src.structure.pattern_adapter.media.audio.AudioPlayer;

/**
 * @author fqxyi
 * @desc 使用 AudioPlayer 来播放不同类型的音频格式。
 * @date 2018/7/24
 */
public class AdapterPattern {

    public static void main(String[] args) {
        AudioPlayer audioPlayer = new AudioPlayer();

        audioPlayer.play("mp3", "beyond the horizon.mp3");
        audioPlayer.play("mp4", "alone.mp4");
        audioPlayer.play("vlc", "far far away.vlc");
        audioPlayer.play("avi", "mind me.avi");
    }

}
