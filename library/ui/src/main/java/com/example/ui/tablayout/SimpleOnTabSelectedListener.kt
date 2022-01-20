package com.example.ui.tablayout

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.ui.R
import com.google.android.material.tabs.TabLayout

/**
 * 简单的常见的CustomView
 *
 * @author: shenbf
 */
class SimpleOnTabSelectedListener(
  private val viewPager: ViewPager2,
  private val onTabSelectedBefore: ((tab: TabLayout.Tab) -> Boolean)? = null,
  private val onTabSelectedAfter: ((tab: TabLayout.Tab) -> Unit)? = null,
  private val onTabUnSelectedBefore: ((tab: TabLayout.Tab) -> Boolean)? = null,
  private val onTabUnSelectedAfter: ((tab: TabLayout.Tab) -> Unit)? = null
) : TabLayout.OnTabSelectedListener {

  private var textSizeUnSelect: Float = 0F
  private var textColorUnSelect: Int = 0
  private var textStyleUnSelect: Int = 0
  private var textSizeSelect: Float = 0F
  private var textColorSelect: Int = 0
  private var textStyleSelect: Int = 0

  override fun onTabSelected(tab: TabLayout.Tab?) {
    if (tab == null) return
    if (onTabSelectedBefore?.invoke(tab) == true) return
    //
    initText(tab, null, textSizeSelect, textColorSelect, textStyleSelect)
    //
    viewPager.setCurrentItem(tab.position, true)
    //
    onTabSelectedAfter?.invoke(tab)
  }

  override fun onTabUnselected(tab: TabLayout.Tab?) {
    if (tab == null) return
    if (onTabUnSelectedBefore?.invoke(tab) == true) return
    //
    initText(tab, null, textSizeUnSelect, textColorUnSelect, textStyleUnSelect)
    //
    onTabUnSelectedAfter?.invoke(tab)
  }

  override fun onTabReselected(tab: TabLayout.Tab?) {}

  fun initCustomView(context: Context, tab: TabLayout.Tab) {
    tab.customView = View.inflate(context, R.layout.layout_tab_text, null)
  }

  fun initTextUnSelect(tab: TabLayout.Tab, text: String? = null, textSize: Float, textColor: Int, textStyle: Int = Typeface.NORMAL) {
    textSizeUnSelect = textSize
    textColorUnSelect = textColor
    textStyleUnSelect = textStyle
    //
    initText(tab, text, textSize, textColor, textStyle)
  }

  fun initTextSelect(tab: TabLayout.Tab, text: String? = null, textSize: Float, textColor: Int, textStyle: Int = Typeface.BOLD) {
    textSizeSelect = textSize
    textColorSelect = textColor
    textStyleSelect = textStyle
    //
    initText(tab, text, textSize, textColor, textStyle)
  }

  private fun initText(tab: TabLayout.Tab, text: String? = null, textSize: Float, textColor: Int, textStyle: Int) {
    val tlTabText = tab.customView!!.findViewById<TextView>(R.id.tl_tab_text)
    text?.let {
      tlTabText.text = text
    }
    tlTabText.textSize = textSize
    tlTabText.setTextColor(tab.customView!!.context.resources.getColor(textColor))
    tlTabText.typeface = Typeface.defaultFromStyle(textStyle)
  }

}