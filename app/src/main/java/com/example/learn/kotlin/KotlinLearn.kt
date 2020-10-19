package com.example.learn.kotlin

/**
 * @author fqxyi
 * @date 2020/9/22
 */

fun main() {
    println("hello world")
    var name: String?
    name = null
    name?.let {
        var length = it.length
    }
    var length = name?.length
    var firstName = "Android"
    var lastName = "Developer"
    val message = "My name is $firstName $lastName"
    val text = if (length ?: 0 > 5) {
        "length > 5"
    } else {
        "length <= 5"
    }
    if (length in 0..300) {

    }
    var scope = 0
    var grade = when(scope) {
        9, 10 -> "Excellent"
        in 6..8 -> "Good"
        4,5 -> "OK"
        in 1..3 -> "Fail"
        else -> "Error"
    }
    for (i in 0..9) {}
    for (i in 0 until 9) {}
    for (i in 9 downTo 0) {}
    for (i in 0..9 step 2) {}
    for (i in 9 downTo 0 step 2) {}
    val collection = ArrayList<String>()
    for (item in collection) {}
    val map = HashMap<String, String>()
    for ((key, value) in map) {}
    val listOfNumber = listOf(1, 2, 3, 4)
    val keyValue = mapOf(1 to "Android", 2 to "iOS")
    collection.forEach {
        println(it)
    }
    collection.filter { it > "1" }.forEach { print(it) }
    val a = 0
    val b = 0
    val andResult = a and b
    val orResult = a or b
    val xorResult = a xor b
    val rightResult = a shr b
    val leftResult = a shl b
    val unsignedRightShift = a ushr b
    val text2 = """
        |firstLine
        |secondLine
    """.trimMargin()
    println(text2)
    val asc = Array(5) { i -> (i * i).toString() }
    asc.forEach { print("$it ") }
    val x = intArrayOf(1, 2, 3)
    val arr = IntArray(5)
    val arr2 = IntArray(5) {42}
    var arr3 = IntArray(5) { it * 1 }
    for (item in arr) {
        println(item)
    }
    for (i in arr.indices) {
        println(i + arr[i])
    }
    for ((index, item) in arr.withIndex()) {
        println("$index , $item")
    }
    arr.forEach {
        println(it)
    }
    arr.forEachIndexed {index, item ->
        println(item)
    }
    //不可变集合
    val list = listOf("1", "2", "1")
    println(list)
    val set = setOf("1", "2", "3")
    println(set)
    //可变集合
    val numbers = mutableListOf(1, 2, 3)
    numbers.add(4)
    numbers.removeAt(1)
    numbers[0] = 0
    println(numbers)
    //ArrayList也是可变集合
    val numberRegex = "\\d+".toRegex()
    val numbers2 = listOf("abc", "123", "456").filter(numberRegex::matches)



}

class Test1 {
    fun isOdd(x: Int) = x % 2 != 0

    fun test() {
        var list = listOf(1, 2, 3, 4, 5)
        println(list.filter(this::isOdd))
    }
}

interface Comparator<T> {
    fun compare(first: T): Int
}

fun sortedByComparator(strings: List<String>, comparator: Comparator<String>) =
    strings.sortedBy(comparator::compare)

class Foo {
    fun bar() {}

    fun test() = ::bar  // equivalent to "this::bar"
}

fun doSomething() {

}

fun doSomething(vararg numbers: Int) {

}

class Utils private constructor() {
    companion object {
        fun getScope(value: Int) : Int {
            return value * 2
        }
    }
}

object Utils2 {
    fun getScope(value: Int) : Int {
        return value * 2
    }
}

data class Model(val name: String, val age: Int)