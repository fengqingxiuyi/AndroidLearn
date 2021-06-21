package com.example.learn.leetcode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @author fqxyi
 * @date 4/28/21
 */
public class TreeTest {

    /**
     * 构建二叉树
     *
     * @param inputList 输入序列
     */
    public static TreeNode createBinaryTree(LinkedList<Integer> inputList) {
        TreeNode node = null;
        if (inputList == null || inputList.isEmpty()) {
            return null;
        }
        Integer data = inputList.removeFirst();
        if (data != null) {
            node = new TreeNode(data);
            node.leftChild = createBinaryTree(inputList);
            node.rightChild = createBinaryTree(inputList);
        }
        return node;
    }

    /**
     * 二叉树前序遍历
     * 3 2 9 10 8 4
     *
     * @param node 二叉树节点
     */
    public static void preOrderTraveral(TreeNode node) {
        if (node == null) {
            return;
        }
        System.out.println(node.data);
        preOrderTraveral(node.leftChild);
        preOrderTraveral(node.rightChild);
    }

    /**
     * 二叉树中序遍历
     * 9 2 10 3 8 4
     *
     * @param node 二叉树节点
     */
    public static void inOrderTraveral(TreeNode node) {
        if (node == null) {
            return;
        }
        inOrderTraveral(node.leftChild);
        System.out.println(node.data);
        inOrderTraveral(node.rightChild);
    }

    /**
     * 二叉树后序遍历
     * 9 10 2 4 8 3
     *
     * @param node 二叉树节点
     */
    public static void postOrderTraveral(TreeNode node) {
        if (node == null) {
            return;
        }
        postOrderTraveral(node.leftChild);
        postOrderTraveral(node.rightChild);
        System.out.println(node.data);
    }

    /**
     * 二叉树非递归前序遍历
     *
     * @param root 二叉树根节点
     */
    public static void preOrderTraveralWithStack(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = root;
        while (treeNode != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈
            while (treeNode != null) {
                System.out.println(treeNode.data);
                stack.push(treeNode);
                treeNode = treeNode.leftChild;
            }
            //如果节点没有左孩子，则弹出栈顶节点，访问节点右孩子
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                treeNode = treeNode.rightChild;
            }
        }
    }

    /**
     * 二叉树非递归中序遍历
     *
     * @param root 二叉树根节点
     */
    public static void inOrderTraveralWithStack(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = root;
        while (treeNode != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.leftChild;
            }
            //如果节点没有左孩子，则弹出栈顶节点，访问节点右孩子
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                System.out.println(treeNode.data);
                treeNode = treeNode.rightChild;
            }
        }
    }

    /**
     * 二叉树非递归后序遍历
     *
     * @param root 二叉树根节点
     */
    public static void postOrderTraveralWithStack(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = root;
        TreeNode preNode = null;  // 用于记录上一次访问的节点
        while (treeNode != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.leftChild;
            }
            //如果节点没有左孩子，则弹出栈顶节点，访问节点右孩子
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                if (treeNode.rightChild == null || preNode == treeNode.rightChild) { //访问节点的条件
                    System.out.println(treeNode.data);
                    preNode = treeNode; // 这一步是记录上一次访问的节点
                    treeNode = null; // 此处为了跳过下一次循环的访问左子节点的过程，直接进入栈的弹出阶段，因为但凡在栈中的节点，它们的左子节点都肯定被经过且已放入栈中。
                } else { //不访问节点的条件
                    stack.push(treeNode); //将已弹出的根节点放回栈中
                    treeNode = treeNode.rightChild;
                }
            }
        }
    }

    /**
     * 二叉树层序非递归遍历
     *
     * @param root 二叉树根节点
     */
    public static void levelOrderTraversalWithQueue(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.println(node.data);
            if (node.leftChild != null) {
                queue.offer(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.offer(node.rightChild);
            }
        }
    }

    /**
     * 二叉树层序遍历
     *
     * @param root 二叉树根节点
     */
    public static void levelOrderTraversal(TreeNode root) {
        if (root == null) return;
        int dep = levelOrderTraversalDepth(root);
        for (int i = 1; i <= dep; i++) {
            _levelOrderTraversal(root, i);
        }
    }

    public static int levelOrderTraversalDepth(TreeNode root) {
        if (root == null) return 0;
        return Math.max(levelOrderTraversalDepth(root.leftChild) + 1, levelOrderTraversalDepth(root.rightChild) + 1);
    }

    private static void _levelOrderTraversal(TreeNode root, int i) {
        if (root == null || i == 0) {
            return;
        }
        if (i == 1) {
            System.out.println(root.data);
            return;
        }
        _levelOrderTraversal(root.leftChild, i - 1);
        _levelOrderTraversal(root.rightChild, i - 1);
    }


    public static void main(String[] args) {
        LinkedList<Integer> inputList = new LinkedList<Integer>
                (Arrays.asList(new Integer[]{3, 2, 9, null, null, 10, null, null, 8, null, 4}));
        TreeNode treeNode = createBinaryTree(inputList);
        System.out.println(" 层序遍历:");
        levelOrderTraversal(treeNode);
        System.out.println(" 层序非递归遍历:");
        levelOrderTraversalWithQueue(treeNode);
        System.out.println(" 前序遍历:");
        preOrderTraveral(treeNode);
        System.out.println(" 前序非递归遍历:");
        preOrderTraveralWithStack(treeNode);
        System.out.println(" 中序遍历:");
        inOrderTraveral(treeNode);
        System.out.println(" 中序非递归遍历:");
        inOrderTraveralWithStack(treeNode);
        System.out.println(" 后序遍历:");
        postOrderTraveral(treeNode);
        System.out.println(" 后序非递归遍历:");
        postOrderTraveralWithStack(treeNode);
    }

    private static class TreeNode {
        int data;
        TreeNode leftChild;
        TreeNode rightChild;

        TreeNode(int data) {
            this.data = data;
        }
    }

}
