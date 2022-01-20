package com.example.learn.single.kotlin

import com.example.log.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/17
 * 懒汉式，编译成Java代码大致如下：
public final class SingleBWithParam {
    private final String str;
    private static volatile SingleBWithParam instance;
    public static final SingleBWithParam.Companion Companion = new SingleBWithParam.Companion((DefaultConstructorMarker)null);

    public final void single() {
        String var1 = "class SingleBWithParam " + this.str + " companion object single";
        boolean var2 = false;
        LogUtil.i(var1);
    }

    private SingleBWithParam(String str) {
        this.str = str;
    }

    // $FF: synthetic method
    public SingleBWithParam(String str, DefaultConstructorMarker $constructor_marker) {
        this(str);
    }

    public static final class Companion {
        @NotNull
        public final SingleBWithParam getInstance(@NotNull String str) {
            Intrinsics.checkParameterIsNotNull(str, "str");
            if (SingleBWithParam.instance == null) {
                boolean var2 = false;
                boolean var3 = false;
                synchronized(this) {
                    int var4 = false;
                    if (SingleBWithParam.instance == null) {
                        SingleBWithParam.instance = new SingleBWithParam(str, (DefaultConstructorMarker)null);
                    }

                    Unit var6 = Unit.INSTANCE;
                }
            }

            SingleBWithParam var10000 = SingleBWithParam.instance;
            if (var10000 == null) {
                Intrinsics.throwNpe();
            }

            return var10000;
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
class SingleBWithParam private constructor(private val str: String) {

    companion object {
        @Volatile
        private var instance: SingleBWithParam? = null

        fun getInstance(str: String): SingleBWithParam {
            if (instance == null) {
                synchronized(SingleBWithParam::class.java) {
                    if (instance == null) {
                        instance =
                            SingleBWithParam(str)
                    }
                }
            }
            return instance!!
        }
    }

    fun single() {
        LogUtil.i("kotlin class SingleBWithParam $str companion object single")
    }

}