package com.example.learn.algorithm

import com.example.utils.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/15
 */
object AlgorithmUtil {

    fun swap(array: IntArray, left: Int, right: Int) {
        val temp: Int = array[left]
        array[left] = array[right]
        array[right] = temp
    }

    fun printOrigin(array: IntArray) {
        LogUtil.i("\t原始数据 ", false)
        printArr(array)
    }

    fun printSort(array: IntArray) {
        LogUtil.i("\t排序数据 ", false)
        printArr(array)
    }

    private fun printArr(array: IntArray) {
        for (i in array) {
            LogUtil.i("$i ", false)
        }
        LogUtil.i("")
    }

}