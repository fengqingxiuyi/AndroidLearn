package com.example.learn.algorithm

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
        print("\t原始数据 ")
        print(array)
    }

    fun printSort(array: IntArray) {
        print("\t排序数据 ")
        print(array)
    }

    private fun print(array: IntArray) {
        for (i in array) {
            print("$i ")
        }
        println()
    }

}