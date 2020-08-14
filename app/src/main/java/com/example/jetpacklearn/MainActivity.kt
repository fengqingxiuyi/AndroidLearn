package com.example.jetpacklearn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpacklearn.annotation.BindRuntime
import com.example.jetpacklearn.annotation.Binding
import com.example.jetpacklearn.constraint.ConstraintActivity
import com.example.jetpacklearn.koin.MyViewModel
import com.example.jetpacklearn.room.AppDatabase
import com.example.jetpacklearn.room.User
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    // Lazy Inject ViewModel
//    val myViewModel: MyViewModel by viewModel()
    val myViewModel: MyViewModel by viewModel { parametersOf("koin from activity") }
    //运行时注解
    @BindRuntime(R.id.bindText) var bindText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testAnnotation()
        testRoom()
        testKoin()
    }

    private fun testAnnotation() {
        //运行时注解
        Binding.bindRuntime(this)
        //效果验证
        bindText?.text = "Binding Success"
    }

    private fun testKoin() {
        Log.i("KOIN_TEST", myViewModel.sayHello())
    }

    /**
     * 问题：1(未解决). 使用小写的thread开启线程，有时候会报java.lang.NullPointerException:
     * Attempt to invoke virtual method 'void java.lang.ThreadGroup.addUnstarted()' on a null object reference
     *
     * 其他：ROOM需要借助kapt，在编译时根据注解(如@Dao、@Database)生成实现类，实现类路径如下：
     * build/generated/source/kapt/debug/com/example/jetpacklearn/room/
     */
    private fun testRoom() {
        Thread({
            val database = AppDatabase.getInstance(this)
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

    fun openConstraint(view: View) {
        startActivity(Intent(this, ConstraintActivity::class.java))
    }
}