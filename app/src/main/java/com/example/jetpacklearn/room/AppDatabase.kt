package com.example.jetpacklearn.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author fqxyi
 * @date 2020/8/13
 */
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            return instance ?: synchronized(this) {
                instance ?:
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "user.db")
                    //.allowMainThreadQueries() //允许主线程操作数据库
                    .build()
            }
        }
    }

    abstract fun userDao(): UserDao

}