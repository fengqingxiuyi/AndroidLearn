package com.example.learn.algorithm;

class Solution {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(7);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(9);
        mirrorTree(root);
        System.out.println(root);
    }
    public static TreeNode mirrorTree(TreeNode root) {
        if(root == null) return null;
        TreeNode tmp = root.left;
        root.left = mirrorTree(root.right);
        root.right = mirrorTree(tmp);
        return root;
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}

//public class Solution {
//
//    public static void main(String[] args) {
//        ListNode node = new ListNode(1);
//        node.next = new ListNode(3);
//        node.next = new ListNode(2);
//        int[] result = reversePrint(node);
//        for (int i=0; i<result.length; i++) {
//            System.out.println(result[i]);
//        }
//    }
//
//    static Stack<Integer> stack = new Stack();
//    public static int[] reversePrint(ListNode head) {
//        if (head == null) {
//            return new int[]{};
//        }
//        ListNode next = head;
//        while(next != null) {
//            stack.push(next.val);
//            next = next.next;
//        }
//        int[] result = new int[stack.size()];
//        for (int i=0; i<stack.size(); i++) {
//            result[i] = stack.pop();
//        }
//        return result;
//    }
//
//    public static class ListNode {
//        int val;
//        ListNode next;
//        ListNode(int x) { val = x; }
//    }
//}
