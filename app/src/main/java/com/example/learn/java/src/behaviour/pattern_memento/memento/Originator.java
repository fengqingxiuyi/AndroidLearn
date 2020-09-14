package com.example.learn.java.src.behaviour.pattern_memento.memento;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/31
 */
public class Originator {

    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public Memento saveStateToMemento() {
        return new Memento(state);
    }

    public void getStateFromMemento(Memento Memento) {
        state = Memento.getState();
    }

}
