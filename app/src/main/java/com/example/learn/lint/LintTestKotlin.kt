package com.example.learn.lint

import android.util.Log

/**
 * @author fqxyi
 * @date 2020/8/18
 * lint功能验证示例
 */
class LintTestKotlin {

    //包含lint的字符串会有警告提示
    private val s1 = "Ignore non-word usages: linting"
    private val s2 = "Let's say it: lint"

    private fun testLint() {
        Log.i("tag", "msg")
        println("sout")
    }

}