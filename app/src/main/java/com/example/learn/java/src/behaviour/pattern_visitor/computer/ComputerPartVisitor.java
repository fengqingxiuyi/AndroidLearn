package com.example.learn.java.src.behaviour.pattern_visitor.computer;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/31
 */
public interface ComputerPartVisitor {

    void visit(Computer computer);
    void visit(Mouse mouse);
    void visit(Keyboard keyboard);
    void visit(Monitor monitor);

}
