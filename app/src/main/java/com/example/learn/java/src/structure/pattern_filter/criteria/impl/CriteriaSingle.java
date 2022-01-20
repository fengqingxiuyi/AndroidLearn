package com.example.learn.java.src.structure.pattern_filter.criteria.impl;

import com.example.learn.java.src.structure.pattern_filter.criteria.Criteria;
import com.example.learn.java.src.structure.pattern_filter.criteria.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fqxyi
 * @desc 创建实现了 Criteria 接口的实体类
 * @date 2018/7/24
 */
public class CriteriaSingle implements Criteria {

    @Override
    public List<Person> meetCriteria(List<Person> persons) {
        List<Person> singlePersons = new ArrayList<Person>();
        for (Person person : persons) {
            if(person.getMaritalStatus().equalsIgnoreCase("SINGLE")){
                singlePersons.add(person);
            }
        }
        return singlePersons;
    }

}
