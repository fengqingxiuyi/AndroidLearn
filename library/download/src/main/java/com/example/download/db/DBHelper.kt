package com.example.download.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * @author fqxyi
 * @描述: 创建数据库
 * @date 2018/4/20
 */
class DBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DROP)
        db.execSQL(SQL_CREATE)
    }

    companion object {
        private const val DB_NAME = "download.db"
        private const val VERSION = 1
        private const val SQL_CREATE =
            "create table thread_info(_id integer primary key autoincrement, thread_id integer, url text, start integer, end integer, finished integer)"
        private const val SQL_DROP = "drop table if exists thread_info"
    }
}