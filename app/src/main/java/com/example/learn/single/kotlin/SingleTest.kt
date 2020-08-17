package com.example.learn.single.kotlin

import com.example.learn.single.java.SingleDoubleCheck
import com.example.learn.single.java.SingleHungry
import com.example.learn.single.java.SingleInner

/**
 * @author fqxyi
 * @date 2020/8/17
 */
fun main() {
    ////kotlin
    //
    SingleA.single()
    //
    SingleB.single()
    //
    single()
    //
    SingleBWithParam.getInstance("xx").single()
    ////java
    //
    SingleHungry.getInstance().single()
    //
    SingleInner.SingleInnerHolder.getInstance().single()
    //
    SingleDoubleCheck.getInstance().single()
}