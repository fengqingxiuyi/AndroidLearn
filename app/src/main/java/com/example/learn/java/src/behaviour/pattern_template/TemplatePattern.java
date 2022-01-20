package com.example.learn.java.src.behaviour.pattern_template;

import com.example.learn.java.src.behaviour.pattern_template.game.Cricket;
import com.example.learn.java.src.behaviour.pattern_template.game.Football;
import com.example.learn.java.src.behaviour.pattern_template.game.Game;

/**
 * @author fqxyi
 * @desc 在模板模式（Template Pattern）中，一个抽象类公开定义了执行它的方法的方式/模板。它的子类可以按需要重写方法实现，但调用将以抽象类中定义的方式进行。这种类型的设计模式属于行为型模式。
 * @date 2018/7/31
 */
public class TemplatePattern {

    public static void main(String[] args) {
        Game game = new Cricket();
        game.play();
        System.out.println();
        game = new Football();
        game.play();
    }

}
