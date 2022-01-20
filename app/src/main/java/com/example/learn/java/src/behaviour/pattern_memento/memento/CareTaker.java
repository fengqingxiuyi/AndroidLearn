package com.example.learn.java.src.behaviour.pattern_memento.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class CareTaker {

    private List<Memento> mementoList = new ArrayList<Memento>();

    public void add(Memento state){
        mementoList.add(state);
    }

    public Memento get(int index){
        return mementoList.get(index);
    }

}
