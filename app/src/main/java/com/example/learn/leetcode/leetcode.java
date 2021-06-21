package com.example.learn.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * @author fqxyi
 * @date 4/12/21
 */
public class leetcode {

    public static ListNode reverseList(ListNode head) {
        return recur(head, null);    // 调用递归并返回
    }

    private static ListNode recur(ListNode cur, ListNode pre) {
        if (cur == null) return pre; // 终止条件
        ListNode res = recur(cur.next, cur);  // 递归后继节点
        cur.next = pre;              // 修改节点引用指向
        return res;                  // 返回反转链表的头节点
    }

    public static int strToInt(String str) {
        StringBuilder builder = new StringBuilder();
        boolean positive = true;
        boolean flag = false;
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (c == ' ') {
                if (builder.length() != 0) {
                    break;
                }
            } else if (c == '+') {
                if (flag) {
                    break;
                }
                flag = true;
            } else if (c == '-') {
                if (flag) {
                    break;
                }
                positive = false;
                flag = true;
            } else if (c >= '0' && c <= '9') {
                builder.append(c);
            } else { //非法字符
                break;
            }
        }
        if (builder.length() == 0) {
            return 0;
        } else {
            // 123 ==>
            // 0*10+1=1
            // 1*10+2=12
            // 12*10+3=123
            String validStr = builder.toString();
            int sum = 0;
            boolean valid = true;
            for (int i=0; i<validStr.length(); i++) {
                sum = sum * 10 + validStr.charAt(i) - '0';
                if (sum > Integer.MAX_VALUE / 10 || (Integer.MAX_VALUE/10 == sum && i < validStr.length() - 1 && Integer.MAX_VALUE % 10 < validStr.charAt(i+1) - '0')) {
                    valid = false;
                    if (positive) {
                        sum = Integer.MAX_VALUE;
                    } else {
                        sum = Integer.MIN_VALUE;
                    }
                    break;
                }
            }
            if (valid) {
                return positive ? sum : -sum;
            } else {
                return sum;
            }
        }
    }

    public static void main(String[] args) {

        strToInt("2147483646");

//        MaxQueue obj = new MaxQueue();
//        obj.push_back(1);
//        obj.push_back(2);
//        int param_1 = obj.max_value();
//        int param_3 = obj.pop_front();
//        int param_2 = obj.max_value();

//        reverseLeftWords("abcdef", 2);

//        MinStack stack = new MinStack();
//        stack.push(2);
//        stack.push(0);
//        stack.push(3);
//        stack.push(0);
//        stack.min();
//        stack.pop();
//        stack.min();
//        stack.pop();
//        stack.min();
//        stack.pop();
//        stack.min();

//        ListNode n1 = new ListNode(1);
//        ListNode n2 = new ListNode(2);
//        ListNode n3 = new ListNode(3);
//        n1.next = n2;
//        n2.next = n3;
//        reverseList(n1);

//        CQueue obj = new CQueue();
//        System.out.println(obj.deleteHead());
//        obj.appendTail(5);
//        obj.appendTail(2);
//        System.out.println(obj.deleteHead());
//        System.out.println(obj.deleteHead());


//        ListNode head = new ListNode(1);
//        ListNode n2 = new ListNode(3);
//        ListNode n3 = new ListNode(2);
//        head.next = n2;
//        n2.next = n3;
//        n3.next = null;
//        int[] result = reversePrint(head);
//        for (int i=0; i<result.length; i++) {
//            System.out.print(result[i] + " ");
//        }
    }

    public static String reverseLeftWords(String s, int n) {
        int sLen = s.length();
        char[] src = new char[sLen];
        for (int i = 0; i < sLen; i++) {
            src[i] = s.charAt(i);
        }
        int remain = sLen % n;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sLen; i++) {
            if (i + n >= sLen && i <= sLen - 1 && remain != 0) {
                char temp = src[i];
                src[i] = src[sLen - 1];
                src[sLen - 1] = temp;
            } else {
                char temp = src[i];
                src[i] = src[i + n];
                src[i + n] = temp;
            }
            //
            result.append(src[i]);
        }
        return result.toString();
    }

    public static String replaceSpace(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                result.append("%20");
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static int[] reversePrint(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        int size = list.size();
        int[] result = new int[size];
        for (int i = 0, j = size - 1; j >= 0; i++, j--) {
            result[i] = list.get(j);
        }
        return result;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

}
