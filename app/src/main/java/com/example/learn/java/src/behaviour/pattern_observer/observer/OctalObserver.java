package com.example.learn.java.src.behaviour.pattern_observer.observer;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class OctalObserver extends Observer{

    public OctalObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println( "Octal String: "
                + Integer.toOctalString( subject.getState() ) );
    }
}
