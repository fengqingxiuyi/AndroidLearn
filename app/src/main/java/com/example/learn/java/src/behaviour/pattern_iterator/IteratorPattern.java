package com.example.learn.java.src.behaviour.pattern_iterator;

import com.example.learn.java.src.behaviour.pattern_iterator.iterator.Iterator;
import com.example.learn.java.src.behaviour.pattern_iterator.iterator.NameRepository;

/**
 * @author fqxyi
 * @desc 迭代器模式（Iterator Pattern）是 Java 和 .Net 编程环境中非常常用的设计模式。这种模式用于顺序访问集合对象的元素，不需要知道集合对象的底层表示。
 * 使用 NameRepository 来获取迭代器，并打印名字
 * @date 2018/7/31
 */
public class IteratorPattern {

    public static void main(String[] args) {
        NameRepository namesRepository = new NameRepository();

        for (Iterator iter = namesRepository.getIterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            System.out.println("Name : " + name);
        }
    }

}
