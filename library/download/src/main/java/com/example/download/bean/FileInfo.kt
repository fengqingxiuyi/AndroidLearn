package com.example.download.bean

import java.io.Serializable

/**
 * @author fqxyi
 * @date 2018/4/20
 */
data class FileInfo(
    var id: Int,
    var url: String,
    var fileName: String,
    var length: Int,
    var finished: Int
) : Serializable {

    companion object {
        const val KEY = "FileInfo"
    }

}