package com.example.ui.dialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.ui.R

/**
 * @author fqxyi
 * @date 2020/8/20
 * 最基本的弹窗组件
 */
abstract class DialogBaseFragment : DialogFragment() {

    protected lateinit var mActivity: Activity
    protected lateinit var mContext: Context
    private var rootView: View? = null

    private var bottom = true
    private var fromBottom = true

    var width = ViewGroup.LayoutParams.MATCH_PARENT
    var height = ViewGroup.LayoutParams.WRAP_CONTENT

    private var cancelable = true
    var canceledOnTouchOutside = true

    private var isShowing = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
        mContext = mActivity.applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialog = dialog ?: return null
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        rootView = View.inflate(context, getContentView(), null)
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        initView(rootView!!)
        return rootView
    }

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)
        this.cancelable = cancelable
    }

    override fun isCancelable(): Boolean {
        return cancelable
    }

    @LayoutRes
    abstract fun getContentView(): Int
    abstract fun initView(view: View)

    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val window = dialog.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wlp = window.attributes
        if (bottom) {
            wlp.gravity = Gravity.BOTTOM
            window.attributes = wlp
        }
        if (fromBottom) {
            window.setWindowAnimations(R.style.BaseDialog_Anim)
        }
        window.setLayout(width, height)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        isShowing = false
    }

    fun setBottom(bottom: Boolean) {
        this.bottom = bottom
    }

    fun setFromBottom(fromBottom: Boolean) {
        this.fromBottom = fromBottom
    }

    fun show(activity: FragmentActivity) {
        if (isShowing) {
            return
        }
        isShowing = true
        try {
            if (isAdded) {
                return
            }
            if (activity.isFinishing) {
                return
            }
            show(activity.supportFragmentManager, javaClass.simpleName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

}