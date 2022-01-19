package com.example.common.ui.empty.bean

import androidx.annotation.DrawableRes
import java.io.Serializable

/**
 * 略
 *
 * @author: shenbf
 */
data class TipContentBean(
  @DrawableRes val imageRes: Int,
  val text: String,
  val textHint: String,
  val btnContent: String,
  val backgroundColor: Int? = null
) : Serializable