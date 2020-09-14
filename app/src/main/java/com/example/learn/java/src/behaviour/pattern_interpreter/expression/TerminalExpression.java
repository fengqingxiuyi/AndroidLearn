package com.example.learn.java.src.behaviour.pattern_interpreter.expression;

/**
 * @author ShenBF
 * @desc 创建实现了上述接口的实体类
 * @date 2018/7/31
 */
public class TerminalExpression implements Expression {

    private String data;

    public TerminalExpression(String data) {
        this.data = data;
    }

    @Override
    public boolean interpret(String context) {
        if (context.contains(data)) {
            return true;
        }
        return false;
    }

}
