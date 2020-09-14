package com.example.learn.java.src.behaviour.pattern_visitor;

import com.example.learn.java.src.behaviour.pattern_visitor.computer.Computer;
import com.example.learn.java.src.behaviour.pattern_visitor.computer.ComputerPart;
import com.example.learn.java.src.behaviour.pattern_visitor.computer.ComputerPartDisplayVisitor;

/**
 * @author ShenBF
 * @desc 在访问者模式（Visitor Pattern）中，我们使用了一个访问者类，它改变了元素类的执行算法。
 * 通过这种方式，元素的执行算法可以随着访问者改变而改变。这种类型的设计模式属于行为型模式。根据模式，元素对象已接受访问者对象，这样访问者对象就可以处理元素对象上的操作。
 * @date 2018/7/31
 */
public class VisitorPattern {

    public static void main(String[] args) {
        ComputerPart computer = new Computer();
        computer.accept(new ComputerPartDisplayVisitor());
    }

}
