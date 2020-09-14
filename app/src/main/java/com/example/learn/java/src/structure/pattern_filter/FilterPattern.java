package com.example.learn.java.src.structure.pattern_filter;

import com.example.learn.java.src.structure.pattern_filter.criteria.Criteria;
import com.example.learn.java.src.structure.pattern_filter.criteria.bean.Person;
import com.example.learn.java.src.structure.pattern_filter.criteria.impl.AndCriteria;
import com.example.learn.java.src.structure.pattern_filter.criteria.impl.CriteriaFemale;
import com.example.learn.java.src.structure.pattern_filter.criteria.impl.CriteriaMale;
import com.example.learn.java.src.structure.pattern_filter.criteria.impl.CriteriaSingle;
import com.example.learn.java.src.structure.pattern_filter.criteria.impl.OrCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenBF
 * @desc 使用不同的标准（Criteria）和它们的结合来过滤 Person 对象的列表。
 * @date 2018/7/24
 */
public class FilterPattern {

    public static void main(String[] args) {
        List<Person> persons = new ArrayList<Person>();

        persons.add(new Person("Robert", "Male", "Single"));
        persons.add(new Person("John", "Male", "Married"));
        persons.add(new Person("Laura", "Female", "Married"));
        persons.add(new Person("Diana", "Female", "Single"));
        persons.add(new Person("Mike", "Male", "Single"));
        persons.add(new Person("Bobby", "Male", "Single"));

        Criteria male = new CriteriaMale();
        Criteria female = new CriteriaFemale();
        Criteria single = new CriteriaSingle();
        Criteria singleMale = new AndCriteria(single, male);
        Criteria singleOrFemale = new OrCriteria(single, female);

        System.out.println("Males: ");
        printPersons(male.meetCriteria(persons));

        System.out.println("\nFemales: ");
        printPersons(female.meetCriteria(persons));

        System.out.println("\nSingle Males: ");
        printPersons(singleMale.meetCriteria(persons));

        System.out.println("\nSingle Or Females: ");
        printPersons(singleOrFemale.meetCriteria(persons));
    }

    public static void printPersons(List<Person> persons) {
        for (Person person : persons) {
            System.out.println("Person : [ Name : " + person.getName()
                    + ", Gender : " + person.getGender()
                    + ", Marital Status : " + person.getMaritalStatus()
                    + " ]");
        }
    }

}
