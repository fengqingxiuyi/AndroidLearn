package com.example.utils.other

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import java.util.*

// 英文
const val LANGUAGE_EN_CODE = 0

// 阿拉伯
const val LANGUAGE_AR_CODE = 1

// 英文
const val LANGUAGE_EN = "en"

// 阿拉伯
const val LANGUAGE_AR = "ar"
const val LANGUAGE_UG = "ug"

val languageCode: Int
  get() {
    return when (locale.language) {
      LANGUAGE_EN -> LANGUAGE_EN_CODE
      LANGUAGE_AR, LANGUAGE_UG -> LANGUAGE_AR_CODE
      else -> LANGUAGE_EN_CODE
    }
  }

/**
 * CountryName，通过系统语言获取
 */
val language: String
  get() {
    return if (locale.language.isNotEmpty()) {
      locale.language
    } else {
      LANGUAGE_EN
    }
  }


/**
 * 获取系统Local信息
 */
val locale: Locale
  get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    LocaleList.getDefault().get(0)
  } else {
    Locale.getDefault()
  }

/**
 * CountryName，优先通过SIM卡运营商获取
 */
fun country(context: Context): String {
  val countryCode = (context.getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso
  if (!countryCode.isNullOrBlank()) {
    return countryCode.uppercase()
  }
  return locale.country
}
