package com.example.learn.java.src.behaviour.pattern_null.customer;

/**
 * @author ShenBF
 * @desc
 * @date 2018/7/31
 */
public class NullCustomer extends AbstractCustomer {

    @Override
    public String getName() {
        return "Not Available in Customer Database";
    }

    @Override
    public boolean isNil() {
        return true;
    }

}
