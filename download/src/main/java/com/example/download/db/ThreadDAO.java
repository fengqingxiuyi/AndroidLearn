package com.example.download.db;

import com.example.download.bean.ThreadInfo;

import java.util.List;

/**
 * @author ShenBF
 * @描述: 数据访问接口
 * @date 2018/4/21
 */
public interface ThreadDAO {

    /**
     * 插入线程信息
     */
    public void insertThread(ThreadInfo threadInfo);

    /**
     * 删除线程信息
     */
    public void deleteThread(String url, int thread_id);

    /**
     * 更新线程下载进度
     */
    public void updateThread(String url, int thread_id, int finished);

    /**
     * 查询文件的线程信息
     */
    public List<ThreadInfo> getThreads(String url);

    /**
     * 线程信息是否存在
     */
    public boolean isExists(String url, int thread_id);

}
