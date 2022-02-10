package com.example.learn.ui.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.common.base.BaseActivity
import com.example.common.utils.viewBinding
import com.example.learn.R
import com.example.learn.databinding.ActivityFragmentTestBinding
import com.example.utils.ext.startAnim

/**
 * 略
 *
 * @author: shenbf
 */
class TestFragmentActivity : BaseActivity() {

  private val binding: ActivityFragmentTestBinding by viewBinding()
  //当前选中的导航位置
  var curSelectNavPosition = 0
  private var fragmentList = mutableListOf<Fragment>()

  companion object {
    const val POSITION_WAHA = 0
    const val POSITION_ROOM = 1
    const val POSITION_MESSAGES = 2
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("curSelectNavPosition", curSelectNavPosition)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    if (savedInstanceState != null) {
      curSelectNavPosition = savedInstanceState.getInt("curSelectNavPosition")
    }
    initView()
  }

  private fun initView() {
    initViewPager()
    initBottomNav()
  }

  private fun initViewPager() {
    fragmentList.add(WahaFragment())
    fragmentList.add(RoomFragment())
    fragmentList.add(MessagesFragment())
    supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragmentList[curSelectNavPosition]).commit()
  }

  private fun initBottomNav() {
    bottomNavShowAnim()
    initBottomNavEvent()
  }

  /**
   * 代码执行顺序不可变
   */
  private fun initBottomNavEvent() {
    binding.mainBottomWahaIcon.setOnClickListener {
      if (curSelectNavPosition == POSITION_WAHA) return@setOnClickListener
      //隐藏之前选中导航的动画效果
      bottomNavHideAnim()
      //显示正确的Pager，位置顺序不可变
      setShowedViewPager(POSITION_WAHA)
      //显示选中导航的动画效果
      bottomNavShowAnim()
    }
    binding.mainBottomRoomIcon.setOnClickListener {
      if (curSelectNavPosition == POSITION_ROOM) return@setOnClickListener
      //隐藏之前选中导航的动画效果
      bottomNavHideAnim()
      //显示正确的Pager，位置顺序不可变
      setShowedViewPager(POSITION_ROOM)
      //显示选中导航的动画效果
      bottomNavShowAnim()
    }
    binding.mainBottomMessagesIcon.setOnClickListener {
      if (curSelectNavPosition == POSITION_MESSAGES) return@setOnClickListener
      //隐藏之前选中导航的动画效果
      bottomNavHideAnim()
      //显示正确的Pager
      setShowedViewPager(POSITION_MESSAGES)
      //显示选中导航的动画效果
      bottomNavShowAnim()
    }
  }

  private fun bottomNavHideAnim() {
    when (curSelectNavPosition) {
      POSITION_WAHA -> {
        binding.mainBottomWahaIcon.startAnim(R.drawable.anim_waha_hide)
        binding.mainBottomWahaText.setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_unselect))
      }
      POSITION_ROOM -> {
        binding.mainBottomRoomIcon.startAnim(R.drawable.anim_room_hide)
        binding.mainBottomRoomText.setTextColor(
          ContextCompat.getColor(this, R.color.bottom_nav_unselect)
        )
      }
      POSITION_MESSAGES -> {
        binding.mainBottomMessagesIcon.startAnim(R.drawable.anim_messages_hide)
        binding.mainBottomMessagesText.setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_unselect))
      }
    }
  }

  private fun bottomNavShowAnim() {
    when (curSelectNavPosition) {
      POSITION_WAHA -> {
        binding.mainBottomWahaIcon.startAnim(R.drawable.anim_waha_show)
        binding.mainBottomWahaText.setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_select))
      }
      POSITION_ROOM -> {
        binding.mainBottomRoomIcon.startAnim(R.drawable.anim_room_show)
        binding.mainBottomRoomText.setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_select))
      }
      POSITION_MESSAGES -> {
        binding.mainBottomMessagesIcon.startAnim(R.drawable.anim_messages_show)
        binding.mainBottomMessagesText.setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_select))
      }
    }
  }

  private fun setShowedViewPager(newPoi: Int) {
    val fragmentBegin = supportFragmentManager.beginTransaction().hide(fragmentList[curSelectNavPosition])
    if (fragmentList[newPoi].isAdded) {
      fragmentBegin.show(fragmentList[newPoi]).commit()
    } else {
      fragmentBegin.add(R.id.frame_layout, fragmentList[newPoi]).commit()
    }
    curSelectNavPosition = newPoi
  }

}