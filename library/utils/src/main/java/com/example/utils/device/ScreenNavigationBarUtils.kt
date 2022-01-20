package com.example.utils.device

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager

internal object ScreenNavigationBarUtils {


  private val BRAND = Build.BRAND.toLowerCase()

  fun isXiaomi(): Boolean {
    return Build.MANUFACTURER.toLowerCase() == "xiaomi"
  }

  fun isVivo(): Boolean {
    return BRAND.contains("vivo")
  }

  fun isOppo(): Boolean {
    return BRAND.contains("oppo") || BRAND.contains("realme")
  }

  fun isHuawei(): Boolean {
    return BRAND.contains("huawei") || BRAND.contains("honor")
  }

  fun isOneplus(): Boolean {
    return BRAND.contains("oneplus")
  }

  fun isSamsung(): Boolean {
    return BRAND.contains("samsung")
  }

  fun isSmartisan(): Boolean {
    return BRAND.contains("smartisan")
  }

  fun isNokia(): Boolean {
    return BRAND.contains("nokia")
  }

  fun isGoogle(): Boolean {
    return BRAND.contains("google")
  }

  fun getRealScreenHeight(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val dm = DisplayMetrics()
    display.getRealMetrics(dm)
    return dm.heightPixels
  }

  fun getRealScreenWidth(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val dm = DisplayMetrics()
    display.getRealMetrics(dm)
    return dm.widthPixels
  }

  fun getScreenHeight(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm)
    return dm.heightPixels
  }


  /**
   * 判断设备是否显示NavigationBar
   *
   * @return 其他值 不显示 0显示 -1 未知
   */
  fun isNavBarHide(context: Context): Int {
    // 有虚拟键，判断是否显示
    if (isVivo()) {
      return vivoNavigationEnabled(context)
    }
    if (isOppo()) {
      return oppoNavigationEnabled(context)
    }
    if (isXiaomi()) {
      return xiaomiNavigationEnabled(context)
    }
    if (isHuawei()) {
      return huaWeiNavigationEnabled(context)
    }
    if (isOneplus()) {
      return oneplusNavigationEnabled(context)
    }
    if (isSamsung()) {
      return samsungNavigationEnabled(context)
    }
    if (isSmartisan()) {
      return smartisanNavigationEnabled(context)
    }
    if (isNokia()) {
      return nokiaNavigationEnabled(context)
    }
    return if (isGoogle()) {
      // navigation_mode 三种模式均有导航栏，只是高度不同。
      0
    } else -1
  }

  /**
   * 判断当前系统是使用导航键还是手势导航操作
   *
   * @param context
   * @return 0 表示使用的是虚拟导航键，1 表示使用的是手势导航，默认是0
   */
  fun vivoNavigationEnabled(context: Context): Int {
    return Settings.Secure.getInt(context.contentResolver, "navigation_gesture_on", 0)
  }

  fun oppoNavigationEnabled(context: Context): Int {
    return Settings.Secure.getInt(context.contentResolver, "hide_navigationbar_enable", 0)
  }

  fun xiaomiNavigationEnabled(context: Context): Int {
    return Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0)
  }

  private fun huaWeiNavigationEnabled(context: Context): Int {
    return Settings.Global.getInt(context.contentResolver, "navigationbar_is_min", 0)
  }

  /**
   * @param context
   * @return 0虚拟导航键  2为手势导航
   */
  private fun oneplusNavigationEnabled(context: Context): Int {
    val result = Settings.Secure.getInt(context.contentResolver, "navigation_mode", 0)
    if (result == 2) {
      // 两种手势 0有按钮， 1没有按钮
      if (Settings.System.getInt(
          context.contentResolver,
          "buttons_show_on_screen_navkeys",
          0
        ) != 0
      ) {
        return 0
      }
    }
    return result
  }

  fun samsungNavigationEnabled(context: Context): Int {
    return Settings.Global.getInt(context.contentResolver, "navigationbar_hide_bar_enabled", 0)
  }

  fun smartisanNavigationEnabled(context: Context): Int {
    return Settings.Global.getInt(context.contentResolver, "navigationbar_trigger_mode", 0)
  }

  fun nokiaNavigationEnabled(context: Context): Int {
    val result = (Settings.Secure.getInt(
      context.contentResolver,
      "swipe_up_to_switch_apps_enabled",
      0
    ) != 0 || Settings.System.getInt(context.contentResolver, "navigation_bar_can_hiden", 0) != 0)
    return if (result) {
      1
    } else {
      0
    }
  }


  fun getNavigationBarHeight(context: Context): Int {
    if (isNavBarHide(context) != 0) return 0
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
      context.resources.getDimensionPixelSize(resourceId)
    } else 0
  }

  private fun isAllScreenDevice(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      // 7.0放开限制，7.0以下都不为全面屏
      false
    } else {
      val realWidth = getRealScreenWidth(context)
      val realHeight = getRealScreenHeight(context)
      val width: Float
      val height: Float
      if (realWidth < realHeight) {
        width = realWidth.toFloat()
        height = realHeight.toFloat()
      } else {
        width = realHeight.toFloat()
        height = realWidth.toFloat()
      }
      // Android中默认的最大屏幕纵横比为1.86
      height / width >= 1.86f
    }
  }

  /**
   * 获取去除导航栏高度的剩余高度（含状态栏）
   * @param context
   * @return
   */
  fun getScreenContentHeight(context: Context): Int {
    return if (isAllScreenDevice(context)) {
      val result = isNavBarHide(context)
      if (result == 0) {
        getRealScreenHeight(context) - getNavigationBarHeight(context)
      } else if (result == -1) {
        // 未知
        getScreenHeight(context)
      } else {
        getRealScreenHeight(context)
      }
    } else {
      getScreenHeight(context)
    }
  }
}