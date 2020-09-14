package com.example.learn.java.src.structure.pattern_filter.criteria.impl;

import com.example.learn.java.src.structure.pattern_filter.criteria.Criteria;
import com.example.learn.java.src.structure.pattern_filter.criteria.bean.Person;

import java.util.List;

/**
 * @author ShenBF
 * @desc 创建实现了 Criteria 接口的实体类
 * @date 2018/7/24
 */
public class AndCriteria implements Criteria {

    private Criteria criteria;
    private Criteria otherCriteria;

    public AndCriteria(Criteria criteria, Criteria otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    @Override
    public List<Person> meetCriteria(List<Person> persons) {
        List<Person> firstCriteriaPersons = criteria.meetCriteria(persons);
        return otherCriteria.meetCriteria(firstCriteriaPersons);
    }

}
