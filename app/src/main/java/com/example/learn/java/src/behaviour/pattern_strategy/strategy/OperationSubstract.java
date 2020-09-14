package com.example.learn.java.src.behaviour.pattern_strategy.strategy;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/31
 */
public class OperationSubstract implements Strategy {

    @Override
    public int doOperation(int num1, int num2) {
        return num1 - num2;
    }

}
