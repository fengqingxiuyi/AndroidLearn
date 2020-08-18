package com.example.learn.jetpack.room

import android.content.Context
import android.util.Log

/**
 * @author fqxyi
 * @date 2020/8/18
 */
object RoomTest {

    /**
     * 问题：1(未解决). 使用小写的thread开启线程，有时候会报java.lang.NullPointerException:
     * Attempt to invoke virtual method 'void java.lang.ThreadGroup.addUnstarted()' on a null object reference
     *
     * 其他：ROOM需要借助kapt，在编译时根据注解(如@Dao、@Database)生成实现类，实现类路径如下：
     * build/generated/source/kapt/debug/com/example/learn/room/
     */
    fun testRoom(context: Context) {
        Thread({
            val database = AppDatabase.getInstance(context)
            val dao = database.userDao()
            dao.insertAll(
                User(1, "zhang", "san"),
                User(2, "li", "si")
            )
            Log.i("ROOM_TEST", dao.findByName("li", "si").toString())
            Log.i("ROOM_TEST", dao.getAll().toString())
            dao.delete(User(1, "zhang", "san"))
            Log.i("ROOM_TEST", dao.getAll().toString())
            Log.i("ROOM_TEST", dao.loadAllByIds(intArrayOf(2)).toString())
        }, "ROOM_TEST").start()
    }

}