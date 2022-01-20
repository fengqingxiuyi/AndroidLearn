package com.example.learn.java.src.structure.pattern_adapter.media.advanced.impl;

import com.example.learn.java.src.structure.pattern_adapter.media.advanced.IAdvancedMediaPlayer;

/**
 * @author fqxyi
 * @desc 实现了 IAdvancedMediaPlayer 接口的实体类
 * @date 2018/7/24
 */
public class Mp4Player implements IAdvancedMediaPlayer {

    @Override
    public void playVlc(String fileName) {
        //什么也不做
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: "+ fileName);
    }

}
