package com.example.learn.java.src.behaviour.pattern_state.state;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/31
 */
public class StopState implements State {

    public void doAction(Context context) {
        System.out.println("Player is in stop state");
        context.setState(this);
    }

    public String toString() {
        return "Stop State";
    }

}
