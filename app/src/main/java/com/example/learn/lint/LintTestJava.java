package com.example.learn.lint;

import android.util.Log;

/**
 * @author fqxyi
 * @date 2020/8/18
 * lint功能验证示例
 */
public class LintTestJava {

    //包含lint的字符串会有警告提示
    private static String s1 = "Ignore non-word usages: linting";
    private static String s2 = "Let's say it: lint";

    private void testLint() {
        Log.i("tag", "msg");
        System.out.println("sout");
    }

}
