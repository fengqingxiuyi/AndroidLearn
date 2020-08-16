package com.example.learn.algorithm;

/**
 * @author fqxyi
 * @date 2020/8/15
 * 插入排序：
 * 将第一待排序序列第一个元素看做一个有序序列，把第二个元素到最后一个元素当成是未排序序列。
 * 从头到尾依次扫描未排序序列，将扫描到的每个元素插入有序序列的适当位置。
 * （如果待插入的元素与有序序列中的某个元素相等，则将待插入元素插入到相等元素的后面。）
 * 3, 7, 2, 9, 1, 4, 6, 8, 10, 5 => 3, 7比较
 * 3, 7, 2, 9, 1, 4, 6, 8, 10, 5 => 2, 7比较
 * 3, 2, 7, 9, 1, 4, 6, 8, 10, 5 => 2, 3比较
 * 2, 3, 7, 9, 1, 4, 6, 8, 10, 5 => 9, 7比较
 * ...
 */
public class Insert {

    public static void sort(int[] array) {
        AlgorithmUtil.INSTANCE.printOrigin(array);
        int len = array.length;
        for (int i = 1; i < len; i++) {
            for (int j = i; j > 0; j--) {
                if (array[j-1] > array[j]) {
                    AlgorithmUtil.INSTANCE.swap(array, j-1, j);
                }
            }
        }
        AlgorithmUtil.INSTANCE.printSort(array);
    }

}
