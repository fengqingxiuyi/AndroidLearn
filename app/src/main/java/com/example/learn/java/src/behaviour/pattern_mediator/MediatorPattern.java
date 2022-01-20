package com.example.learn.java.src.behaviour.pattern_mediator;

import com.example.learn.java.src.behaviour.pattern_mediator.chat.User;

/**
 * @author fqxyi
 * @desc 中介者模式（Mediator Pattern）是用来降低多个对象和类之间的通信复杂性。这种模式提供了一个中介类，该类通常处理不同类之间的通信，并支持松耦合，使代码易于维护。中介者模式属于行为型模式。
 * 使用 User 对象来显示他们之间的通信
 * @date 2018/7/31
 */
public class MediatorPattern {

    public static void main(String[] args) {
        User robert = new User("Robert");
        User john = new User("John");

        robert.sendMessage("Hi! John!");
        john.sendMessage("Hello! Robert!");
    }

}
