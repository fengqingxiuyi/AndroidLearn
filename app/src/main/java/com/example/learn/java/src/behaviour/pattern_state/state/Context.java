package com.example.learn.java.src.behaviour.pattern_state.state;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class Context {

    private State state;

    public Context() {
        state = null;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

}
