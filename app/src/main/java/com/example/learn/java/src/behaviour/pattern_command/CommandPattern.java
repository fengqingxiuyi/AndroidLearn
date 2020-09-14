package com.example.learn.java.src.behaviour.pattern_command;

import com.example.learn.java.src.behaviour.pattern_command.order.Broker;
import com.example.learn.java.src.behaviour.pattern_command.order.BuyStock;
import com.example.learn.java.src.behaviour.pattern_command.order.SellStock;
import com.example.learn.java.src.behaviour.pattern_command.order.Stock;

/**
 * @author ShenBF
 * @desc 命令模式
 * 命令模式（Command Pattern）是一种数据驱动的设计模式，它属于行为型模式。
 * 请求以命令的形式包裹在对象中，并传给调用对象。调用对象寻找可以处理该命令的合适的对象，并把该命令传给相应的对象，该对象执行命令。
 *
 * 使用 Broker 类来接受并执行命令
 * @date 2018/7/31
 */
public class CommandPattern {

    public static void main(String[] args) {
        Stock abcStock = new Stock();

        BuyStock buyStockOrder = new BuyStock(abcStock);
        SellStock sellStockOrder = new SellStock(abcStock);

        Broker broker = new Broker();
        broker.takeOrder(buyStockOrder);
        broker.takeOrder(sellStockOrder);

        broker.placeOrders();
    }

}
