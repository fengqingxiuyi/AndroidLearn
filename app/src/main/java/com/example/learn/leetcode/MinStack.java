package com.example.learn.leetcode;

import java.util.Stack;

/**
 * @author fqxyi
 * @date 4/13/21
 */
public class MinStack {

    /** initialize your data structure here. */

    Stack<Integer> stack;
    int min;

    public MinStack() {
        stack = new Stack<>();
    }

    public void push(int x) {
        if (min > x) {
            min = x;
        }
        stack.push(x);
    }

    public void pop() {
        stack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int min() {
//        int min = 0;
//        for (int i=0; i<stack.size(); i++) {
//            if (min > stack.get(i)) {
//                min = stack.get(i);
//            }
//        }
        return min;
    }
}
