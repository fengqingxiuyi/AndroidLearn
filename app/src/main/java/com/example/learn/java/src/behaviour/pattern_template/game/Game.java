package com.example.learn.java.src.behaviour.pattern_template.game;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/31
 */
public abstract class Game {

    abstract void initialize();

    abstract void startPlay();

    abstract void endPlay();

    //模板
    public final void play() {
        //初始化游戏
        initialize();
        //开始游戏
        startPlay();
        //结束游戏
        endPlay();
    }

}
