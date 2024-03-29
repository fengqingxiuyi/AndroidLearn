package com.example.learn.single.kotlin

import com.example.log.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/17
 * 饿汉式，编译成Java代码大致如下：
    public final class SingleA {
        public static final SingleA INSTANCE;

        public final void single() {
            String var1 = "object SingleA single";
            boolean var2 = false;
            LogUtil.i(var1);
        }

        private SingleA() {
        }

        static {
            SingleA var0 = new SingleA();
            INSTANCE = var0;
        }
    }
 */
object SingleA {

    fun single() {
        LogUtil.i("kotlin object SingleA single")
    }

}