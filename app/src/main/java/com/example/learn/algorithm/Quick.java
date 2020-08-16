package com.example.learn.algorithm;

/**
 * @author fqxyi
 * @date 2020/8/15
 * 快速排序：
 * 通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，
 * 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
 * []内数值为基准值
 * ()内数值为已排序数值
 * 3，7，2，9，1，4，6，8，10，[5] //选择最后一个数值为基准值。start 0 end 9 base 9。未排序部分从左往右，找到第一个比基准值大的，进行交换。
 * (3)，[5]，2，9，1，4，6，8，10，(7) //start 1 end 8 base 1。未排序部分从右往左，找到第一个比基准值小的，进行交换。
 * (3，4)，2，9，1，[5]，(6，8，10，7) //start 2 end 5 base 5。未排序部分从左往右，找到第一个比基准值大的，进行交换。
 * (3，4，2)，[5]，1，(9，6，8，10，7) //start 3 end 4 base 3。未排序部分从右往左，找到第一个比基准值小的，进行交换。
 * (3，4，2，1)，[5]，(9，6，8，10，7) //基准值两侧都已排序，两侧数据再递归拆分。start 4 end 4 base 4。
 * ...
 */
public class Quick {

    public static void sort(int[] array) {
        AlgorithmUtil.INSTANCE.printOrigin(array);
        int len = array.length;
        sort(array, 0, len - 1);
        AlgorithmUtil.INSTANCE.printSort(array);
    }

    public static void sort(int[] array, int start, int end) {
        if (start > end) {
            //如果只有一个元素，就不用再排下去了
            return;
        }
        int partition = partitionEnd(array, start, end);
        sort(array, start, partition - 1);
        sort(array, partition + 1, end);
    }

    public static int partitionEnd(int[] array, int start, int end) {
        int base = array[end];
        while (start < end) {
            //从左边开始遍历，如果比基准值小，就继续向右走
            while (start < end && array[start] <= base) {
                start++;
            }
            //上面的while循环结束时，就说明当前的array[start]的值比基准值大，应与基准值进行交换
            if (start < end) {
                AlgorithmUtil.INSTANCE.swap(array, start, end);
                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值右边)，因此右边也要同时向前移动一位
                end--;
            }
            //从右边开始遍历，如果比基准值大，就继续向左走
            while (start < end && array[end] >= base) {
                end--;
            }
            //上面的while循环结束时，就说明当前的array[end]的值比基准值小，应与基准值进行交换
            if (start < end) {
                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值左边)，因此左边也要同时向后移动一位
                AlgorithmUtil.INSTANCE.swap(array, start, end);
                start++;
            }
        }
        return end;
    }

    public static int partitionStart(int[] array, int start, int end) {
        int base = array[start];
        while (start < end) {
            //从右边开始遍历，如果比基准值大，就继续向左走
            while (start < end && array[end] >= base) {
                end--;
            }
            //上面的while循环结束时，就说明当前的array[end]的值比基准值小，应与基准值进行交换
            if (start < end) {
                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值左边)，因此左边也要同时向后移动一位
                AlgorithmUtil.INSTANCE.swap(array, start, end);
                start++;
            }
            //从左边开始遍历，如果比基准值小，就继续向右走
            while (start < end && array[start] <= base) {
                start++;
            }
            //上面的while循环结束时，就说明当前的array[start]的值比基准值大，应与基准值进行交换
            if (start < end) {
                AlgorithmUtil.INSTANCE.swap(array, start, end);
                //交换后，此时的那个被调换的值也同时调到了正确的位置(基准值右边)，因此右边也要同时向前移动一位
                end--;
            }
        }
        return end;
    }

}
