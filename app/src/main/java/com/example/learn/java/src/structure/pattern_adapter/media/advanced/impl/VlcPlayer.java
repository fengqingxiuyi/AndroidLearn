package com.example.learn.java.src.structure.pattern_adapter.media.advanced.impl;

import com.example.learn.java.src.structure.pattern_adapter.media.advanced.IAdvancedMediaPlayer;

/**
 * @author fqxyi
 * @desc 实现了 IAdvancedMediaPlayer 接口的实体类
 * @date 2018/7/24
 */
public class VlcPlayer implements IAdvancedMediaPlayer {

    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: "+ fileName);
    }

    @Override
    public void playMp4(String fileName) {
        //什么也不做
    }

}
