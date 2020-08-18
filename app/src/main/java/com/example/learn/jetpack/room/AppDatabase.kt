package com.example.learn.jetpack.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author fqxyi
 * @date 2020/8/13
 *
 * add exportSchema = false in order to fix
 * Schema export directory is not provided to the annotation processor so we cannot export the schema.
 * You can either provide `room.schemaLocation` annotation processor argument OR set exportSchema to false.
 */
@Database(entities = [User::class], version = 1, exportSchema = false)
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