package com.example.learn.algorithm;

/**
 * @author fqxyi
 * @date 2020/8/15
 * 冒泡排序：比较相邻的元素。如果第一个比第二个大，就交换他们两个
 * 平均时间复杂度：O(n²)
 * 空间复杂度：O(1)
 * 3, 7, 2, 9, 1, 4, 6, 8, 10, 5 => 3, 7比较
 * 3, 7, 2, 9, 1, 4, 6, 8, 10, 5 => 7, 2比较
 * 3, 2, 7, 9, 1, 4, 6, 8, 10, 5 => 7, 9比较
 * 3, 2, 7, 9, 1, 4, 6, 8, 10, 5 => 9, 1比较
 * 3, 2, 7, 1, 9, 4, 6, 8, 10, 5 => 9, 4比较
 * ...
 */
public class Bubbling {

    public static void sort(int[] array) {
        AlgorithmUtil.INSTANCE.printOrigin(array);
        //
        int len = array.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    AlgorithmUtil.INSTANCE.swap(array, j, j + 1);
                }
            }
        }
        //
        AlgorithmUtil.INSTANCE.printSort(array);
    }

}
