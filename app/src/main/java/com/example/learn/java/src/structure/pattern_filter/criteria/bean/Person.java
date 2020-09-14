package com.example.learn.java.src.structure.pattern_filter.criteria.bean;

/**
 * @author ShenBF
 * @desc 在该类上应用标准
 * @date 2018/7/24
 */
public class Person {

    private String name;
    private String gender;
    private String maritalStatus;

    public Person(String name, String gender, String maritalStatus) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

}
