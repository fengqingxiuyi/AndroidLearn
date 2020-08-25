package com.example.download.db

import com.example.download.bean.ThreadInfo

/**
 * @author fqxyi
 * @描述: 数据访问接口
 * @date 2018/4/21
 */
interface ThreadDAO {
    /**
     * 插入线程信息
     */
    fun insertThread(threadInfo: ThreadInfo)

    /**
     * 删除线程信息
     */
    fun deleteThread(url: String?, thread_id: Int)

    /**
     * 更新线程下载进度
     */
    fun updateThread(url: String?, thread_id: Int, finished: Int)

    /**
     * 查询文件的线程信息
     */
    fun getThreads(url: String?): List<ThreadInfo?>?

    /**
     * 线程信息是否存在
     */
    fun isExists(url: String?, thread_id: Int): Boolean
}