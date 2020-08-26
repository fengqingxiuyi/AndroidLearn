package com.example.learn.ui.appbarlayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learn.R
import kotlinx.android.synthetic.main.appbar_layout_fragment.*

class AppbarLayoutFragment : Fragment() {

//    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.appbar_layout_fragment, container, false)
        /**
         * 如果init函数在return之前执行，那么下面这句findViewById不能省略，否则会抛出java.lang.IllegalStateException: Fragment already added异常
         */
//        recycler = view.findViewById<View>(R.id.recycler) as RecyclerView
//        init()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * init函数正确使用位置应该在这里
         */
        init()
    }

    private fun init() {
        context?.let {
            val manager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            recycler.layoutManager = manager
            val adapter = AppbarLayoutRecyclerAdapter(it)
            recycler.adapter = adapter
            adapter.updateData()
        }
    }
}