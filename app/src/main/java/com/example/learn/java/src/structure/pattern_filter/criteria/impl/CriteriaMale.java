package com.example.learn.java.src.structure.pattern_filter.criteria.impl;

import com.example.learn.java.src.structure.pattern_filter.criteria.Criteria;
import com.example.learn.java.src.structure.pattern_filter.criteria.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenBF
 * @desc 创建实现了 Criteria 接口的实体类
 * @date 2018/7/24
 */
public class CriteriaMale implements Criteria {

    @Override
    public List<Person> meetCriteria(List<Person> persons) {
        List<Person> malePersons = new ArrayList<Person>();
        for (Person person : persons) {
            if(person.getGender().equalsIgnoreCase("MALE")){
                malePersons.add(person);
            }
        }
        return malePersons;
    }

}
