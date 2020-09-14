package com.example.learn.java.src.behaviour.pattern_mediator.chat;

import java.util.Date;

/**
 * @author ShenBF
 * @desc 创建中介类
 * @date 2018/7/31
 */
public class ChatRoom {

    public static void showMessage(User user, String message) {
        System.out.println(new Date().toString()
                + " [" + user.getName() + "] : " + message);
    }

}
