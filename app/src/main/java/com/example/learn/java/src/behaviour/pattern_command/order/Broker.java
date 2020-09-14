package com.example.learn.java.src.behaviour.pattern_command.order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenBF
 * @desc 创建命令调用类
 * @date 2018/7/31
 */
public class Broker {

    private List<Order> orderList = new ArrayList<Order>();

    public void takeOrder(Order order) {
        orderList.add(order);
    }

    public void placeOrders() {
        for (Order order : orderList) {
            order.execute();
        }
        orderList.clear();
    }

}
