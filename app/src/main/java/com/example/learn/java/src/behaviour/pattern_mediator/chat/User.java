package com.example.learn.java.src.behaviour.pattern_mediator.chat;

/**
 * @author ShenBF
 * @desc 创建 user 类
 * @date 2018/7/31
 */
public class User {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name) {
        this.name = name;
    }

    public void sendMessage(String message) {
        ChatRoom.showMessage(this, message);
    }

}
