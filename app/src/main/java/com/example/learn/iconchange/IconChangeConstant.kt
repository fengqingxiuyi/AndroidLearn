package com.example.learn.iconchange

interface IconChangeConstant {
    companion object {

        val ACTIVITY_PATH_ARR = arrayOf(
            ".MainActivity",
            ".activityAlias",  //第一个预埋icon
            ".activityAliasSecond" //第二个预埋icon
        )

        const val NORMAL = 1
        const val CHANGE = 2
        const val CHANGE2 = 3
    }
}