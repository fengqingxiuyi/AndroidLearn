package com.example.learn.java.src.behaviour.pattern_state.state;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class StartState implements State {

    public void doAction(Context context) {
        System.out.println("Player is in start state");
        context.setState(this);
    }

    public String toString() {
        return "Start State";
    }

}
