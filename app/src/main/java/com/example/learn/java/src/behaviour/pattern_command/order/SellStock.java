package com.example.learn.java.src.behaviour.pattern_command.order;

/**
 * @author fqxyi
 * @desc 创建实现了 Order 接口的实体类
 * @date 2018/7/31
 */
public class SellStock implements Order {

    private Stock abcStock;

    public SellStock(Stock abcStock) {
        this.abcStock = abcStock;
    }

    public void execute() {
        abcStock.sell();
    }

}
