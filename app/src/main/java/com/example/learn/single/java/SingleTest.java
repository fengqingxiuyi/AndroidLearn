package com.example.learn.single.java;

import com.example.learn.single.kotlin.SingleA;
import com.example.learn.single.kotlin.SingleB;
import com.example.learn.single.kotlin.SingleBWithParam;
import com.example.learn.single.kotlin.SingleCKt;

/**
 * @author fqxyi
 * @date 2020/8/17
 */
public class SingleTest {

    public static void main(String[] args) {
        ////kotlin
        //
        SingleA.INSTANCE.single();
        //
        SingleB.Companion.single();
        //
        SingleCKt.single();
        //
        SingleBWithParam.Companion.getInstance("xx").single();
        ////java
        //
        SingleHungry.getInstance().single();
        //
        SingleInner.getInstance().single();
        //
        SingleDoubleCheck.getInstance().single();
    }

}
