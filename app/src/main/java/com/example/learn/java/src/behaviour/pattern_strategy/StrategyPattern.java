package com.example.learn.java.src.behaviour.pattern_strategy;

import com.example.learn.java.src.behaviour.pattern_strategy.strategy.Context;
import com.example.learn.java.src.behaviour.pattern_strategy.strategy.OperationAdd;
import com.example.learn.java.src.behaviour.pattern_strategy.strategy.OperationMultiply;
import com.example.learn.java.src.behaviour.pattern_strategy.strategy.OperationSubstract;

/**
 * @author fqxyi
 * @desc 在策略模式（Strategy Pattern）中，一个类的行为或其算法可以在运行时更改。这种类型的设计模式属于行为型模式。
 * 在策略模式中，我们创建表示各种策略的对象和一个行为随着策略对象改变而改变的 context 对象。策略对象改变 context 对象的执行算法。
 * @date 2018/7/31
 */
public class StrategyPattern {

    public static void main(String[] args) {
        Context context = new Context(new OperationAdd());
        System.out.println("10 + 5 = " + context.executeStrategy(10, 5));

        context = new Context(new OperationSubstract());
        System.out.println("10 - 5 = " + context.executeStrategy(10, 5));

        context = new Context(new OperationMultiply());
        System.out.println("10 * 5 = " + context.executeStrategy(10, 5));
    }

}
