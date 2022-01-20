package com.example.learn.jetpack.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.log.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/18
 * 输出日志：
2020-08-18 14:56:26.853 32701-32701/com.example.learn I/Worker: testWorkManager start
2020-08-18 14:56:26.863 32701-32701/com.example.learn I/Worker: testWorkManager end
2020-08-18 14:56:26.971 32701-32730/com.example.learn I/Worker: doWork requestType = Second Request
2020-08-18 14:56:26.971 32701-32730/com.example.learn I/Worker: doWork start
2020-08-18 14:56:29.904 32701-32752/com.example.learn I/Worker: doWork requestType = First Request
2020-08-18 14:56:29.904 32701-32752/com.example.learn I/Worker: doWork start
2020-08-18 14:56:31.971 32701-32730/com.example.learn I/Worker: doWork end
2020-08-18 14:56:31.975 32701-32718/com.example.learn I/WM-WorkerWrapper: Worker result SUCCESS for Work [ id=67c361da-d871-466c-8295-7bf049956ba7, tags={ com.example.learn.jetpack.workmanager.MyWorker } ]
2020-08-18 14:56:34.905 32701-32752/com.example.learn I/Worker: doWork end
2020-08-18 14:56:34.909 32701-32723/com.example.learn I/WM-WorkerWrapper: Worker result SUCCESS for Work [ id=529bfa75-2903-4a01-87ea-ba5ca89997c1, tags={ tag, com.example.learn.jetpack.workmanager.MyWorker } ]
2020-08-18 14:56:34.913 32701-32723/com.example.learn I/WM-WorkerWrapper: Setting status to enqueued for 5eb14402-3fc4-4466-9f53-c32e384f4986
2020-08-18 14:56:34.986 32701-32751/com.example.learn I/Worker: doWork requestType = Third Request
2020-08-18 14:56:34.986 32701-32751/com.example.learn I/Worker: doWork start
2020-08-18 14:56:39.986 32701-32751/com.example.learn I/Worker: doWork end
2020-08-18 14:56:39.990 32701-32727/com.example.learn I/WM-WorkerWrapper: Worker result SUCCESS for Work [ id=5eb14402-3fc4-4466-9f53-c32e384f4986, tags={ com.example.learn.jetpack.workmanager.MyWorker } ]
 */
class MyWorker : Worker {

    constructor(context: Context, workerParams: WorkerParameters) : super(context, workerParams)

    /**
     * doWork函数执行在Executor创建的线程中，比如pool-1-thread-1
     */
    override fun doWork(): Result {
        //获取传入的参数
        val requestType = inputData.getString("RequestType")
        LogUtil.i("Worker", "doWork requestType = $requestType")
        //
        LogUtil.i("Worker", "doWork start")
        //延时5秒，模拟耗时任务
        Thread.sleep(5000)
        LogUtil.i("Worker", "doWork end")
        //返回成功
        return Result.success()
        //返回失败
        //return Result.failure()
        //返回重试
        //return Result.retry()
    }
}