package com.example.learn.java.src.behaviour.pattern_template.game;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class Football extends Game {

    @Override
    void endPlay() {
        System.out.println("Football Game Finished!");
    }

    @Override
    void initialize() {
        System.out.println("Football Game Initialized! Start playing.");
    }

    @Override
    void startPlay() {
        System.out.println("Football Game Started. Enjoy the game!");
    }

}
