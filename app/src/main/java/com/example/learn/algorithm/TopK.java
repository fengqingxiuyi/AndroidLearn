package com.example.learn.algorithm;

import com.example.utils.LogUtil;

/**
 * @author fqxyi
 * @date 2020/8/15
 * Top K ：在一堆数据里面找到前 K 大（当然也可以是前 K 小）的数。
 */
public class TopK {

    public static void findMax(int[] array, int k) {
        AlgorithmUtil.INSTANCE.printOrigin(array);
        int max = findMaxTopK(array, k);
        AlgorithmUtil.INSTANCE.printSort(array);
        //
        LogUtil.i("\t找到数据 ", false);
        for (int i = max; i < array.length; i++) {
            LogUtil.i(array[i] + " ", false);
        }
        LogUtil.i("");
    }

    /**
     * 寻找前K大的元素
     */
    private static int findMaxTopK(int[] array, int k) {
        k = array.length - k;
        int start = 0, end = array.length - 1;
        while (start < end) {
            int partition = Quick.partitionEnd(array, start, end);
            if (partition == k) { //找到了第K大的元素
                break;
            } else if (partition < k) { //说明第K大的元素在右边
                start = partition + 1;
            } else { //说明第K大的元素在左边
                end = partition - 1;
            }
        }
        return k;
    }

    public static void findMin(int[] array, int k) {
        AlgorithmUtil.INSTANCE.printOrigin(array);
        int min = findMinTopK(array, k);
        AlgorithmUtil.INSTANCE.printSort(array);
        //
        LogUtil.i("\t找到数据 ", false);
        for (int i = 0; i < min; i++) {
            LogUtil.i(array[i] + " ", false);
        }
        LogUtil.i("");
    }

    /**
     * 寻找前K小的元素
     */
    private static int findMinTopK(int[] array, int k) {
        int start = 0, end = array.length - 1;
        while (start < end) {
            int partition = Quick.partitionStart(array, start, end);
            if (partition == k) { //找到了第K小的元素
                break;
            } else if (partition < k) { //说明第K小的元素在右边
                start = partition + 1;
            } else { //说明第K小的元素在左边
                end = partition - 1;
            }
        }
        return k;
    }

}
