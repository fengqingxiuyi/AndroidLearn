package com.example.learn.leetcode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author fqxyi
 * @date 4/20/21
 */
public class MaxQueue {

    private LinkedList<Integer> list;
    private Map<Integer, Integer> map;

    public MaxQueue() {
        list = new LinkedList<>();
        map = new TreeMap<>();

    }

    public int max_value() {
        if (map.isEmpty()) return -1;
        return map.get(map.size()-1);
        /*
        if (list.isEmpty()) return -1;
        int max = list.get(0);
        for (int i=1; i<list.size(); i++) {
            if (list.get(i) > max) {
                max = list.get(i);
            }
        }
        return max;
        */
    }

    public void push_back(int value) {
        list.offer(value);
        map.put(map.size(), value);
        /*
        list.offer(value);
        */
    }

    public int pop_front() {
        if (list.isEmpty()) return -1;
        int result = list.pop();
        for (int i=0; i<map.size(); i++) {
            if (map.get(i) == result) {
                map.remove(i);
                break;
            }
        }
        return result;
        /*
        if (list.isEmpty()) return -1;
        return list.pop();
        */
    }
}

/**
 * Your MaxQueue object will be instantiated and called as such:
 * MaxQueue obj = new MaxQueue();
 * int param_1 = obj.max_value();
 * obj.push_back(value);
 * int param_3 = obj.pop_front();
 */
