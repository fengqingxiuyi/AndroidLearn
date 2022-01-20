package com.example.learn.java.src.behaviour.pattern_visitor.computer;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class Keyboard  implements ComputerPart {

    @Override
    public void accept(ComputerPartVisitor computerPartVisitor) {
        computerPartVisitor.visit(this);
    }

}
