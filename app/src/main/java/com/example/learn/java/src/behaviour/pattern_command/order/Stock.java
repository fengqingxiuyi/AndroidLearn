package com.example.learn.java.src.behaviour.pattern_command.order;

/**
 * @author ShenBF
 * @desc 创建一个请求类
 * @date 2018/7/31
 */
public class Stock {

    private String name = "ABC";
    private int quantity = 10;

    public void buy() {
        System.out.println("Stock [ Name: " + name + ", Quantity:" + quantity + " ]bought ");
    }

    public void sell() {
        System.out.println("Stock [ Name: " + name + ", Quantity:" + quantity + " ]sold ");
    }

}
