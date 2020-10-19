# Kotlin 

[学习 Kotlin](https://www.kotlincn.net/docs/reference/)

#### 已绑定的可调用引用

现在可以使用`::`操作符来获取指向特定对象实例的方法或属性的[成员引用](https://www.kotlincn.net/docs/reference/reflection.html#%E5%87%BD%E6%95%B0%E5%BC%95%E7%94%A8)。 以前这只能用 lambda 表达式表示。 这里有一个例子：

```kotlin
val numberRegex = "\\d+".toRegex()
val numbers = listOf("abc", "123", "456").filter(numberRegex::matches)

fun main(args: Array<String>) {
    println("Result is $numbers")
}
```

更详细信息请参阅其[KEEP](https://github.com/Kotlin/KEEP/blob/master/proposals/bound-callable-references.md)。