package com.example.ui.tablayout

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout

/**
 * 解决直接使用TabLayoutMediator，但又需要在点击另一个Tab时弹提示的需求，直接用TabLayoutMediator点击Tab时还会切换
 *
 * @author: shenbf
 */
object TabLayoutMediatorFix {

  fun populateTabsFromPagerAdapter(
    viewPager: ViewPager2,
    tabLayout: TabLayout?,
    tabConfigurationStrategy: (tab: TabLayout.Tab, position: Int) -> Unit
  ) {
    if (tabLayout == null) return
    val adapter = viewPager.adapter
    tabLayout.removeAllTabs()
    if (adapter != null) {
      val adapterCount = adapter.itemCount
      for (i in 0 until adapterCount) {
        val tab = tabLayout.newTab()
        tabConfigurationStrategy.invoke(tab, i)
        tabLayout.addTab(tab, false)
      }
      // Make sure we reflect the currently set ViewPager item
      if (adapterCount > 0) {
        val lastItem = tabLayout.tabCount - 1
        val currItem = viewPager.currentItem.coerceAtMost(lastItem)
        if (currItem != tabLayout.selectedTabPosition) {
          tabLayout.selectTab(tabLayout.getTabAt(currItem))
        }
      }
    }
  }

}