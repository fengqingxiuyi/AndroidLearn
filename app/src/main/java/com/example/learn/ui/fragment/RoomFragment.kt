package com.example.learn.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common.base.BaseFragment
import com.example.learn.R

/**
 * 略
 *
 * @author: shenbf
 */
class RoomFragment : BaseFragment() {
  override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
    val rootView: View = inflater.inflate(R.layout.fragment_room, container, false)
    return rootView

  }

  override fun initData(savedInstanceState: Bundle?) {

  }
}