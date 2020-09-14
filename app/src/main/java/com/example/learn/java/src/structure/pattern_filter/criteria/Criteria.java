package com.example.learn.java.src.structure.pattern_filter.criteria;

import com.example.learn.java.src.structure.pattern_filter.criteria.bean.Person;

import java.util.List;

/**
 * @author ShenBF
 * @desc 为标准（Criteria）创建一个接口
 * @date 2018/7/24
 */
public interface Criteria {

    List<Person> meetCriteria(List<Person> persons);

}
