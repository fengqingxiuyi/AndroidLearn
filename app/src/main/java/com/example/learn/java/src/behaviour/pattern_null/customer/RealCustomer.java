package com.example.learn.java.src.behaviour.pattern_null.customer;

/**
 * @author fqxyi
 * @desc
 * @date 2018/7/31
 */
public class RealCustomer extends AbstractCustomer {

    public RealCustomer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNil() {
        return false;
    }

}
