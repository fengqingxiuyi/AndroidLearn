package com.example.learn.java.src.structure.pattern_filter.criteria.impl;

import com.example.learn.java.src.structure.pattern_filter.criteria.Criteria;
import com.example.learn.java.src.structure.pattern_filter.criteria.bean.Person;

import java.util.List;

/**
 * @author ShenBF
 * @desc 创建实现了 Criteria 接口的实体类
 * @date 2018/7/24
 */
public class OrCriteria implements Criteria {

    private Criteria criteria;
    private Criteria otherCriteria;

    public OrCriteria(Criteria criteria, Criteria otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    @Override
    public List<Person> meetCriteria(List<Person> persons) {
        List<Person> firstCriteriaItems = criteria.meetCriteria(persons);
        List<Person> otherCriteriaItems = otherCriteria.meetCriteria(persons);

        for (Person person : otherCriteriaItems) {
            if (!firstCriteriaItems.contains(person)) {
                firstCriteriaItems.add(person);
            }
        }
        return firstCriteriaItems;
    }

}
