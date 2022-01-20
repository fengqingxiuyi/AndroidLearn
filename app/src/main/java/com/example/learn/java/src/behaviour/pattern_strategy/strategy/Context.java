package com.example.learn.java.src.behaviour.pattern_strategy.strategy;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class Context {

    private Strategy strategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public int executeStrategy(int num1, int num2) {
        return strategy.doOperation(num1, num2);
    }

}
