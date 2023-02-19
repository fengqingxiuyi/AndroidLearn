package com.example.ui.text

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.example.ui.utils.DimenUtil
import com.example.ui.utils.isRtlLayout

/**
 * 统一字体 实现 字体边框、渐变、阴影、字体
 *
 * 存在的Bug：emoji兼容性问题，如果出现emoji与省略号共存的情况，无法给省略号准确的描边。
 *
 * @author shenBF
 */
open class UnichatTextView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

  private val TAG = "UnichatTextViewTAG"

  protected val ELLIPSIS = "..."

  //边框相关
  private lateinit var borderTextPaint: TextPaint

  //渐变相关
  private var openGradient = false
  private var orientation: OrientationMode = OrientationMode.VERTICAL
  private var startColor: Int = 0
  private var endColor: Int = 0
  private lateinit var linearHorizontalGradient: LinearGradient //避免在onDraw中频繁创建对象
  private lateinit var linearVerticalGradient: LinearGradient

  //阴影相关
  private var openShadow = false
  private var shadowX = 0f
  private var shadowY = 0f
  private var shadowColorValue: Int = 0
  private var shadowBlur = 0f

  //字体相关
  private val defaultTypeface =
    Typeface.createFromAsset(context.assets, "fonts/unichat_default.ttf")

  init {
    includeFontPadding = false
    typeface = defaultTypeface
  }

  fun notifyChanged() {
    post {
      if (::borderTextPaint.isInitialized) {
        requestLayout()
      }
      invalidate()
    }
  }

  /**
   * 设置字体渐变，仅单行生效
   * @param orientation OrientationMode
   * @param startColor Int
   * @param endColor Int
   */
  fun setGradientOnSingleLine(
    orientation: OrientationMode,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int
  ) {
    openGradient = true
    this.orientation = orientation
    this.startColor = startColor
    this.endColor = endColor
  }

  fun setGradientOnSingleLineAndNotify(
    orientation: OrientationMode,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int
  ) {
    setGradientOnSingleLine(orientation, startColor, endColor)
    notifyChanged()
  }

  /**
   * 设置字体描边，仅单行有效
   * @param color Int
   * @param width Float
   */
  fun setStrokeOnSingleLine(
    @ColorInt color: Int,
    width: Float = DimenUtil.dp2pxFloat(context, 1.5f)
  ) {
    if (!::borderTextPaint.isInitialized) {
      borderTextPaint = TextPaint()
    }
    borderTextPaint.style = Paint.Style.STROKE
    borderTextPaint.color = color
    borderTextPaint.strokeWidth = width
  }

  fun setStrokeOnSingleLineAndNotify(
    @ColorInt color: Int,
    width: Float = DimenUtil.dp2pxFloat(context, 1.5f)
  ) {
    setStrokeOnSingleLine(color, width)
    notifyChanged()
  }

  /**
   * 设置从上往下的阴影
   * @param shadow Float 阴影宽度
   * @param shadowColor Int
   * @param blur Float 模糊程度，值越大越模糊
   */
  fun setTopToBottomShadow(
    shadow: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    setShadow(0f, shadow, shadowColor, blur)
  }

  /**
   * 设置从左右往的阴影
   * @param shadow Float 阴影宽度
   * @param shadowColor Int
   * @param blur Float 模糊程度，值越大越模糊
   */
  fun setLeftToRightShadow(
    shadow: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    setShadow(shadow, 0f, shadowColor, blur)
  }

  private fun setShadow(
    shadowX: Float,
    shadowY: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    openShadow = true
    this.shadowY = shadowX
    this.shadowY = shadowY
    shadowColorValue = shadowColor
    shadowBlur = blur
  }

  private fun setShadowAndNotify(
    shadowX: Float,
    shadowY: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    setShadow(shadowX, shadowY, shadowColor, blur)
    notifyChanged()
  }

  fun removeGradient() {
    openGradient = false
    paint.shader = null
  }

  fun removeGradientThenNotify() {
    removeGradient()
    notifyChanged()
  }

  fun removeShadow() {
    openShadow = false
    paint.clearShadowLayer()
  }

  fun removeShadowThenNotify() {
    removeShadow()
    notifyChanged()
  }

  fun removeStroke() {
    if (!::borderTextPaint.isInitialized) {
      return
    }
    borderTextPaint.reset()
  }

  fun removeStrokeThenNotify() {
    removeStroke()
    notifyChanged()
  }

  fun removeGradientAndShadowThenNotify() {
    removeGradient()
    removeShadow()
    notifyChanged()
  }

  fun removeAllThenNotify() {
    removeGradient()
    removeShadow()
    removeStroke()
    notifyChanged()
  }

  override fun onDraw(canvas: Canvas?) {
    //draw shadow
    if (openShadow) {
      paint.shader = null
      paint.setShadowLayer(shadowBlur, shadowX, shadowY, shadowColorValue)
      super.onDraw(canvas)
    }
    //draw gradient
    if (openGradient) {
      paint.clearShadowLayer()
      when (orientation) {
        OrientationMode.HORIZONTAL -> {
          if (::linearHorizontalGradient.isInitialized) {
            linearHorizontalGradient = LinearGradient(
              0f,
              0f,
              width.toFloat(),
              0f,
              startColor,
              endColor,
              Shader.TileMode.CLAMP
            )
          }
          paint.shader = linearHorizontalGradient

        }
        OrientationMode.VERTICAL -> {
          if (::linearVerticalGradient.isInitialized) {
            linearVerticalGradient = LinearGradient(
              0f,
              0f,
              0f,
              height * 0.7f,
              startColor,
              endColor,
              Shader.TileMode.CLAMP
            )
          }
          paint.shader = linearVerticalGradient

        }
      }
      super.onDraw(canvas)
    }
    if (::borderTextPaint.isInitialized) {
      //复制参数
      borderTextPaint.textSize = paint.textSize
      borderTextPaint.typeface = defaultTypeface
      borderTextPaint.flags = paint.flags
      borderTextPaint.alpha = paint.alpha

      //在文本底层画出带描边的文本
      var text = text.toString()
      if (width != 0 && borderTextPaint.measureText(text) + paddingStart + paddingEnd > width) {
        text = getAdjustText(text, "")
        //再设置一次文本是为了解决边框绘制之后与显示文案不一致的问题
        setText(text)
        //解决再次setText导致的移动（wrap_content时）和位置偏移（原本居中现在不居中）问题
        requestLayout()
      }
      canvas?.drawText(
        text,
        getStartPositionInTextViewOnSingleLine(),
        baseline.toFloat(),
        borderTextPaint
      )
      super.onDraw(canvas)
    }
    //else
    if (!openShadow && !openGradient && !::borderTextPaint.isInitialized) {
      super.onDraw(canvas)
    }
  }

  private fun getStartPositionInTextViewOnSingleLine(): Float {
    var min = 0f
    layout?.let {
      val layout = layout
      val bound = Rect()
      val line = layout.getLineForOffset(0) //获取字符在第几行
      layout.getLineBounds(line, bound) //获取该行的Bound区域

      val x1 = layout.getPrimaryHorizontal(0)
      val x2 = layout.getPrimaryHorizontal(text.length)
      min = if (x1 > x2) {
        x2
      } else {
        x1
      }
    }
    return if (isRtlLayout(context)) {
      min + getDrawableEndWidthAndPadding() + paddingEnd
    } else {
      min + getDrawableStartWidthAndPadding() + paddingStart
    }
  }

  private fun getDrawableStartWidthAndPadding(): Int {
    val drawables: Array<Drawable?> = compoundDrawables
    return if (isRtlLayout(context)) {
      val drawableLeft: Drawable? = drawables[2]
      drawableLeft?.let {
        compoundDrawablePadding + it.intrinsicWidth
      } ?: 0
    } else {
      val drawableRight: Drawable? = drawables[0]
      drawableRight?.let {
        compoundDrawablePadding + it.intrinsicWidth
      } ?: 0
    }
  }

  private fun getDrawableEndWidthAndPadding(): Int {
    val drawables: Array<Drawable?> = compoundDrawables
    return if (isRtlLayout(context)) {
      val drawableLeft: Drawable? = drawables[0]
      drawableLeft?.let {
        compoundDrawablePadding + it.intrinsicWidth
      } ?: 0
    } else {
      val drawableRight: Drawable? = drawables[2]
      drawableRight?.let {
        compoundDrawablePadding + it.intrinsicWidth
      } ?: 0
    }
  }

  enum class OrientationMode(val value: Int) {
    HORIZONTAL(0),
    VERTICAL(1)
  }

  protected fun getAdjustText(originText: String, ellipsis: String): String {
    //计算省略号的宽度
    val ellipsisWidth = if (ellipsis.isEmpty()) 0f else paint.measureText(ellipsis)
    // 为了避免substring删除emoji等多个char的元素导致的错误截取
    var tmpTxt: String? = null
    val codePointCount = originText.codePointCount(0, originText.length)
//    logD(TAG, "codePointCount = $codePointCount")
    for (i in codePointCount downTo 0) {
      val offset = originText.offsetByCodePoints(0, i)
      tmpTxt = originText.substring(0, offset)
      val tmpTxtWidth = paint.measureText(tmpTxt) + paddingStart + paddingEnd
//      logD(TAG, "tmpTxt = $tmpTxt , ellipsisWidth = $ellipsisWidth, tmpTxtWidth = $tmpTxtWidth, width = $width")
      if (tmpTxtWidth + ellipsisWidth <= width) break
    }
    return tmpTxt + ellipsis
  }

}