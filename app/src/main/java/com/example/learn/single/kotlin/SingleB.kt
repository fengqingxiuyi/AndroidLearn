package com.example.learn.single.kotlin

/**
 * @author fqxyi
 * @date 2020/8/17
 * 饿汉式，编译成Java代码大致如下：
    public final class SingleB {
        public static final SingleB.Companion Companion = new SingleB.Companion((DefaultConstructorMarker)null);

        public static final class Companion {
            public final void single() {
                String var1 = "class SingleB companion object single";
                boolean var2 = false;
                System.out.println(var1);
            }

            private Companion() {
            }

            // $FF: synthetic method
            public Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }
        }
    }
 */
class SingleB {

    companion object {

        fun single() {
            println("kotlin class SingleB companion object single")
        }

    }

}