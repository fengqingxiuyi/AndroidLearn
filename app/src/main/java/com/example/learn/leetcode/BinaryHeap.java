package com.example.learn.leetcode;

import java.util.Arrays;

/**
 * @author fqxyi
 * @date 5/7/21
 * 二叉堆
 */
public class BinaryHeap {

    /**
     * childIndex = 2 * parentIndex + 1
     *
     * [0, 1, 2, 6, 3, 7, 8, 9, 10, 5]
     * [1, 5, 2, 6, 7, 3, 8, 9, 10]
     * @param args
     */
    public static void main(String[] args) {
        int[] array = new int[] {1,3,2,6,5,7,8,9,10,0};
        upAdjust(array);
        System.out.println(Arrays.toString(array));

        array = new int[] {7,1,3,10,5,2,8,9,6};
        buildHeap(array);
        System.out.println(Arrays.toString(array));

    }

    /**
     * 构建堆
     * @param array
     */
    private static void buildHeap(int[] array) {
        // 从最后一个非叶子节点开始，依次做“下沉”调整
        for (int i = (array.length-2)/2; i>=0; i--) {
            downAdjust(array, i, array.length);
        }
    }

    /**
     * 下沉
     * @param array
     * @param parentIndex
     * @param length
     */
    private static void downAdjust(int[] array, int parentIndex, int length) {
        int temp = array[parentIndex];
        int childIndex = parentIndex * 2 + 1;
        while (childIndex < length) {
            if (childIndex + 1 < length && array[childIndex + 1] < array[childIndex]) {
                childIndex++;
            }
            if (temp <= array[childIndex]) {
                break;
            }
            array[parentIndex] = array[childIndex];
            parentIndex = childIndex;
            childIndex = parentIndex * 2 + 1;
        }
        array[parentIndex] = temp;
    }

    /**
     * 上浮
     * @param array
     */
    private static void upAdjust(int[] array) {
        int childIndex = array.length - 1;
        int parentIndex = (childIndex - 1) / 2;
        int temp = array[childIndex];
        while (childIndex > 0 && array[parentIndex] > temp) {
            array[childIndex] = array[parentIndex];
            childIndex = parentIndex;
            parentIndex = (parentIndex - 1) / 2;
        }
        array[childIndex] = temp;
    }

}
