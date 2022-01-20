package com.example.learn.java.src.creation.pattern_builder.pack.impl;

import com.example.learn.java.src.creation.pattern_builder.pack.IPack;

/**
 * @author fqxyi
 * @desc 实现 IPack 接口的实体类
 * @date 2018/7/24
 */
public class Wrapper implements IPack {

    @Override
    public String pack() {
        return "包装纸";
    }

}
