package com.example.download.bean

import java.io.Serializable

/**
 * @author fqxyi
 * @date 2018/4/20
 */
data class ThreadInfo(
    //线程ID
    var id: Int,
    //下载地址
    var url: String,
    //下载开始位置
    var start: Int,
    //下载结束位置
    var end: Int,
    //完成进度
    var finished: Int
) : Serializable {

    companion object {
        const val KEY_FINISHED = "finished"
    }

}