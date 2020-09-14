package com.example.learn.java.src.behaviour.pattern_command.order;

/**
 * @author ShenBF
 * @desc 创建实现了 Order 接口的实体类
 * @date 2018/7/31
 */
public class BuyStock implements Order {

    private Stock abcStock;

    public BuyStock(Stock abcStock) {
        this.abcStock = abcStock;
    }

    public void execute() {
        abcStock.buy();
    }

}
