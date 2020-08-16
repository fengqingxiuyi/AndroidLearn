package com.example.learn.algorithm

/**
 * @author fqxyi
 * @date 2020/8/15
 */
fun main() {
    val array = intArrayOf(3, 7, 2, 9, 1, 4, 6, 8, 10, 5)
    println("冒泡排序")
    Bubbling.sort(array.clone())
    println("插入排序")
    Insert.sort(array.clone())
    println("选择排序")
    Select.sort(array.clone())
    println("快速排序")
    Quick.sort(array.clone())
    println("Top K")
    TopK.findMax(array.clone(), 4)
    TopK.findMin(array.clone(), 2)
}