package com.example.learn.ui.adapter

import android.content.Context
import android.widget.TextView
import com.example.learn.R
import com.example.ui.adapter.CommonAdapter
import com.example.ui.adapter.ViewHolder

/**
 * @author fqxyi
 * @date 2017/5/19
 */
class ListCommonAdapter(context: Context?, data: List<String?>?) :
    CommonAdapter<String?>(context, data) {

    override fun getLayoutId(): Int {
        return R.layout.list_common_adapter_item
    }

    override fun convert(holder: ViewHolder?, t: String?, position: Int) {
        // holder.setText(textView, s);

        // 或
        val textView = holder?.findViewById<TextView>(R.id.textView)
        textView?.text = t
    }
}