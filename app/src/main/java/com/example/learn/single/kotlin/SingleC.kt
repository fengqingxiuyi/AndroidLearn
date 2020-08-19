package com.example.learn.single.kotlin

import com.example.utils.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/17
 * 与其说是单例，实际上就是静态函数，
    public final class SingleCKt {
        public static final void single() {
            String var0 = "SingleC single";
            boolean var1 = false;
            System.out.println(var0);
        }
    }
 */
fun single() {
    LogUtil.i("kotlin SingleC single")
}