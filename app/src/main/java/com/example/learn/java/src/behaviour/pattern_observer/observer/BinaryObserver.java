package com.example.learn.java.src.behaviour.pattern_observer.observer;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/31
 */
public class BinaryObserver extends Observer {

    public BinaryObserver(Subject subject) {
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("Binary String: "
                + Integer.toBinaryString(subject.getState()));
    }
}
