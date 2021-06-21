package com.example.learn.leetcode;

import java.util.LinkedList;

/**
 * @author fqxyi
 * @date 4/12/21
 */
class CQueue {

    LinkedList<Integer> stackA, stackB;

    public CQueue() {
        stackA = new LinkedList<>();
        stackB = new LinkedList<>();
    }

    public void appendTail(int value) {
        stackA.addLast(value);
    }

    public int deleteHead() {
        int size = stackA.size();
        for (int i=0; i<size; i++) {
            stackB.addLast(stackA.removeLast());
        }
        if (stackB.isEmpty()) {
            return -1;
        } else {
            return stackB.removeLast();
        }
    }
}

/**
 * Your CQueue object will be instantiated and called as such:
 * CQueue obj = new CQueue();
 * obj.appendTail(value);
 * int param_2 = obj.deleteHead();
 */
