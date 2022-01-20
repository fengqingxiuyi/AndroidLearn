package com.example.learn.java.src.behaviour.pattern_observer.observer;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class HexaObserver extends Observer{

    public HexaObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println( "Hex String: "
                + Integer.toHexString( subject.getState() ).toUpperCase() );
    }
}
