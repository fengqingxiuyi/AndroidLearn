package com.example.learn.algorithm;

/**
 * @author fqxyi
 * @date 2020/8/15
 * 选择排序：
 * 首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置。
 * 再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
 * 重复第二步，直到所有元素均排序完毕。
 * 平均时间复杂度：O(n²)
 * 空间复杂度：O(1)
 * 3, 7, 2, 9, 1, 4, 6, 8, 10, 5 => 1
 * 1, 3, 7, 2, 9, 4, 6, 8, 10, 5 => 2
 * 1, 2, 3, 7, 9, 4, 6, 8, 10, 5 => 3
 * ...
 */
public class Select {

    public static void sort(int[] array) {
        AlgorithmUtil.INSTANCE.printOrigin(array);
        int len = array.length;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (array[i] > array[j]) {
                    AlgorithmUtil.INSTANCE.swap(array, i, j);
                }
            }
        }
        AlgorithmUtil.INSTANCE.printSort(array);
    }

}
