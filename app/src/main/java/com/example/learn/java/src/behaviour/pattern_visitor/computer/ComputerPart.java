package com.example.learn.java.src.behaviour.pattern_visitor.computer;

/**
 * @author fqxyi
 * @desc 定义一个表示元素的接口
 * @date 2018/7/31
 */
public interface ComputerPart {

    void accept(ComputerPartVisitor computerPartVisitor);

}
