package com.example.learn.java.src.behaviour.pattern_interpreter.expression;

/**
 * @author fqxyi
 * @desc 创建实现了上述接口的实体类
 * @date 2018/7/31
 */
public class AndExpression implements Expression {

    private Expression expr1 = null;
    private Expression expr2 = null;

    public AndExpression(Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public boolean interpret(String context) {
        return expr1.interpret(context) && expr2.interpret(context);
    }

}
