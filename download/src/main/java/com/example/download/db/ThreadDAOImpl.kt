package com.example.download.db

import android.content.Context
import com.example.download.bean.ThreadInfo
import java.util.*

/**
 * @author fqxyi
 * @date 2018/4/21
 * 数据访问接口实现
 */
class ThreadDAOImpl(context: Context?) : ThreadDAO {

    private val helper: DBHelper = DBHelper(context)

    override fun insertThread(threadInfo: ThreadInfo) {
        val db = helper.writableDatabase
        db.execSQL(
            "insert into thread_info(thread_id, url, start, end, finished) values(?, ?, ?, ?, ?)",
            arrayOf(
                threadInfo.id,
                threadInfo.url,
                threadInfo.start,
                threadInfo.end,
                threadInfo.finished
            )
        )
        db.close()
    }

    override fun deleteThread(url: String?, thread_id: Int) {
        val db = helper.writableDatabase
        db.execSQL(
            "delete from thread_info where url = ? and thread_id = ?",
            arrayOf(url, thread_id)
        )
        db.close()
    }

    override fun updateThread(
        url: String?,
        thread_id: Int,
        finished: Int
    ) {
        val db = helper.writableDatabase
        db.execSQL(
            "update thread_info set finished = ? where url = ? and thread_id = ?",
            arrayOf(finished, url, thread_id)
        )
        db.close()
    }

    override fun getThreads(url: String?): List<ThreadInfo?>? {
        val db = helper.writableDatabase
        val list: MutableList<ThreadInfo?> =
            ArrayList()
        val cursor =
            db.rawQuery("select * from thread_info where url = ?", arrayOf(url))
        while (cursor.moveToNext()) {
            val threadInfo =
                ThreadInfo(
                    cursor.getInt(cursor.getColumnIndex("thread_id")),
                    cursor.getString(cursor.getColumnIndex("url")),
                    cursor.getInt(cursor.getColumnIndex("start")),
                    cursor.getInt(cursor.getColumnIndex("end")),
                    cursor.getInt(cursor.getColumnIndex("finished"))
                )
            list.add(threadInfo)
        }
        cursor.close()
        db.close()
        return list
    }

    override fun isExists(url: String?, thread_id: Int): Boolean {
        val db = helper.writableDatabase
        val cursor = db.rawQuery(
            "select * from thread_info where url = ? and thread_id = ?",
            arrayOf(url, thread_id.toString())
        )
        val exists = cursor.moveToNext()
        cursor.close()
        db.close()
        return exists
    }

}