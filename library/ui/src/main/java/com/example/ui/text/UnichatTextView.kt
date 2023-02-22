package com.example.ui.text

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.SparseArray
import android.util.SparseIntArray
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.example.ui.utils.DimenUtil
import com.example.ui.utils.isRtlLayout

/**
 * 统一字体 实现 字体边框、渐变、阴影、字体
 *
 * @author shenBF
 */
open class UnichatTextView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

  companion object {
    private const val TAG = "UnichatTextViewTAG"
    //自定义的省略号，当遇到需要省略号显示的场景时，会自己计算能显示的内容并拼接上该省略号
    const val ELLIPSIS = "..."
  }

  //描边相关
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
  private val defaultTypeface by lazy(LazyThreadSafetyMode.NONE) {
    Typeface.createFromAsset(context.assets, "fonts/unichat_default.ttf")
  }

  init {
    includeFontPadding = false

    val a = context.obtainStyledAttributes(attrs, com.example.ui.R.styleable.TextAppearance)
    if (!a.hasValue(com.example.ui.R.styleable.TextAppearance_android_fontFamily)) {
      typeface = defaultTypeface
    }
    a.recycle()
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
   * 设置字体渐变
   * @param orientation OrientationMode 渐变方向
   * @param startColor Int 渐变开始颜色
   * @param endColor Int 渐变结束颜色
   * @param defaultColor Int 应用于Tab切换时，未选中的Tab需要显示该颜色，而不是渐变色
   */
  fun setGradient(
    orientation: OrientationMode,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int,
    @ColorInt defaultColor: Int = Color.WHITE, //非Tab情况随便给个颜色都行，只要不是透明
  ) {
    openGradient = true
    setTextColor(defaultColor) //必须要写这个，否则和投影叠加的时候颜色会被污染
    this.orientation = orientation
    this.startColor = startColor
    this.endColor = endColor
  }

  /**
   * 设置字体渐变，并执行invalidate
   * @param orientation OrientationMode 渐变方向
   * @param startColor Int 渐变开始颜色
   * @param endColor Int 渐变结束颜色
   * @param defaultColor Int 应用于Tab切换时，未选中的Tab需要显示该颜色，而不是渐变色
   */
  fun setGradientThenNotify(
    orientation: OrientationMode,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int,
    @ColorInt defaultColor: Int = Color.WHITE, //非Tab情况随便给个颜色都行，只要不是透明
  ) {
    setGradient(orientation, startColor, endColor, defaultColor)
    notifyChanged()
  }

  /**
   * 设置字体描边
   * @param color Int 描边颜色
   * @param width Float 描边宽度
   */
  fun setStroke(@ColorInt color: Int, width: Float = DimenUtil.dp2pxFloat(context, 1.5f)) {
    if (!::borderTextPaint.isInitialized) {
      borderTextPaint = TextPaint().apply {
        style = Paint.Style.STROKE
      }
    }
    borderTextPaint.color = color
    borderTextPaint.strokeWidth = width
  }

  /**
   * 设置字体描边，并执行requestLayout和invalidate
   * @param color Int 描边颜色
   * @param width Float 描边宽度
   */
  fun setStrokeThenNotify(@ColorInt color: Int, width: Float = DimenUtil.dp2pxFloat(context, 1.5f)) {
    setStroke(color, width)
    notifyChanged()
  }

  /**
   * 设置从上往下的阴影
   * @param shadow Float 阴影在Y轴的偏移量
   * @param shadowColor Int 阴影颜色
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
   * 设置从上往下的阴影，并执行invalidate
   * @param shadow Float 阴影在Y轴的偏移量
   * @param shadowColor Int 阴影颜色
   * @param blur Float 模糊程度，值越大越模糊
   */
  fun setTopToBottomShadowThenNotify(
    shadow: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    setTopToBottomShadow(shadow, shadowColor, blur)
    notifyChanged()
  }

  /**
   * 设置从左右往的阴影
   * @param shadow Float 阴影在X轴的偏移量
   * @param shadowColor Int 阴影颜色
   * @param blur Float 模糊程度，值越大越模糊
   */
  fun setLeftToRightShadow(
    shadow: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    setShadow(shadow, 0f, shadowColor, blur)
  }

  /**
   * 设置从左往右的阴影，并执行invalidate
   * @param shadow Float 阴影在Y轴的偏移量
   * @param shadowColor Int 阴影颜色
   * @param blur Float 模糊程度，值越大越模糊
   */
  fun setLeftToRightShadowThenNotify(
    shadow: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    setLeftToRightShadow(shadow, shadowColor, blur)
    notifyChanged()
  }

  /**
   * 设置自定义方向的阴影
   * @param shadowX Float 阴影在X轴的偏移量
   * @param shadowY Float 阴影在Y轴的偏移量
   * @param shadowColor Int 阴影颜色
   * @param blur Float 模糊程度，值越大越模糊
   */
  fun setShadow(
    shadowX: Float,
    shadowY: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    openShadow = true
    this.shadowX = shadowX
    this.shadowY = shadowY
    shadowColorValue = shadowColor
    shadowBlur = blur
  }

  /**
   * 设置自定义方向的阴影，并执行invalidate
   * @param shadowX Float 阴影在X轴的偏移量
   * @param shadowY Float 阴影在Y轴的偏移量
   * @param shadowColor Int 阴影颜色
   * @param blur Float 模糊程度，值越大越模糊
   */
  fun setShadowThenNotify(
    shadowX: Float,
    shadowY: Float,
    @ColorInt shadowColor: Int,
    blur: Float
  ) {
    setShadow(shadowX, shadowY, shadowColor, blur)
    notifyChanged()
  }

  /**
   * 移除渐变效果
   */
  fun removeGradient() {
    openGradient = false
    paint.shader = null
  }

  /**
   * 移除渐变效果，并执行invalidate
   */
  fun removeGradientThenNotify() {
    removeGradient()
    notifyChanged()
  }

  /**
   * 移除描边
   */
  fun removeStroke() {
    if (!::borderTextPaint.isInitialized) {
      return
    }
    borderTextPaint.reset()
  }

  /**
   * 移除描边，并执行invalidate
   */
  fun removeStrokeThenNotify() {
    if (!::borderTextPaint.isInitialized) {
      return
    }
    removeStroke()
    notifyChanged()
  }

  /**
   * 移除投影效果
   */
  fun removeShadow() {
    openShadow = false
    paint.clearShadowLayer()
  }

  /**
   * 移除投影效果，并执行invalidate
   */
  fun removeShadowThenNotify() {
    removeShadow()
    notifyChanged()
  }

  /**
   * 移除渐变和投影效果，并执行invalidate
   */
  fun removeGradientAndShadowThenNotify() {
    removeGradient()
    removeShadow()
    notifyChanged()
  }

  /**
   * 移除所有效果，并执行requestLayout和invalidate
   */
  fun removeAllThenNotify() {
    removeGradient()
    removeShadow()
    removeStroke()
    notifyChanged()
  }

  override fun onDraw(canvas: Canvas?) {
    if (canvas == null) {
      super.onDraw(null)
      return
    }
    //draw shadow
    if (openShadow) {
      drawShadow()
      super.onDraw(canvas)
    }
    //draw gradient
    if (openGradient) {
      drawGradient()
      super.onDraw(canvas)
    }
    //draw stroke
    if (::borderTextPaint.isInitialized) {
      drawStroke(canvas)
      super.onDraw(canvas)
    }
    //else
    if (!openShadow && !openGradient && !::borderTextPaint.isInitialized) {
      super.onDraw(canvas)
    }
  }

  private fun drawShadow() {
    paint.shader = null //清除gradient效果
    paint.setShadowLayer(shadowBlur, shadowX, shadowY, shadowColorValue)
  }

  private fun drawGradient() {
    paint.clearShadowLayer()
    when (orientation) {
      OrientationMode.HORIZONTAL -> {
        if (!::linearHorizontalGradient.isInitialized) {
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
        if (!::linearVerticalGradient.isInitialized) {
          linearVerticalGradient = LinearGradient(
            0f,
            0f,
            0f,
            lineHeight * 1f,
            startColor,
            endColor,
            Shader.TileMode.REPEAT
          )
        }
        paint.shader = linearVerticalGradient
      }
    }
  }

  private fun drawStroke(canvas: Canvas) {
    val layout = layout ?: return
    //复制参数
    borderTextPaint.textSize = paint.textSize
    borderTextPaint.typeface = defaultTypeface
    borderTextPaint.flags = paint.flags
    borderTextPaint.alpha = paint.alpha

    //在文本底层画出带描边的文本
    val lineCount = lineCount
    getLineOffsetInfo(layout, lineCount).apply {
      drawStrokeText(canvas, layout, first, second)
    }
  }

  private fun getLineOffsetInfo(layout: Layout, lineCount: Int): Pair<SparseArray<StringBuilder>, SparseIntArray> {
    val offsetList = SparseArray<StringBuilder>(lineCount)
    val offsetIndexList = SparseIntArray(lineCount)
    offsetIndexList.put(0, 0)
    val availableWidth = width - paddingStart - paddingEnd
    //使用layout.text，不要使用text，因为如果ellipsize为start之类的，layout.text就是显示文案
    layout.text.forEachIndexed { index, c ->
      val line = layout.getLineForOffset(index) //获取字符在第几行
      if (offsetList.get(line) == null) {
        offsetList.put(line, StringBuilder(""))
      }
      if (availableWidth < layout.getLineMax(line)) { //字符串内容长度超过了显示区域，这种情况是ellipsize为none的时候
        if (paint.measureText(offsetList.get(line).toString()) < availableWidth) {
          offsetList.put(line, offsetList.get(line).append(c))
        }
      } else {
        offsetList.put(line, offsetList.get(line).append(c))
      }
      if (line > 0 && offsetIndexList[line] == 0) {
        offsetIndexList.put(line, index)
      }
    }
    return Pair(offsetList, offsetIndexList)
  }

  private fun drawStrokeText(
    canvas: Canvas,
    layout: Layout,
    offsetList: SparseArray<StringBuilder>,
    offsetIndexList: SparseIntArray
  ) {
    for (line in 0 until lineCount) {
      val position = getCharPositionInTextView(layout, offsetIndexList, line)
      canvas.drawText(
        offsetList[line].toString(),
        position.first,
        position.second + baseline.toFloat(),
        borderTextPaint
      )
    }
  }

  private fun getCharPositionInTextView(
    layout: Layout,
    offsetIndexList: SparseIntArray,
    index: Int
  ): Pair<Float, Int> {
    val bound = Rect()
    val line = layout.getLineForOffset(offsetIndexList[index]) //获取字符在第几行
    layout.getLineBounds(line, bound) //获取该行的Bound区域
    val top = layout.getLineTop(line)
    val offset = if (offsetIndexList.size() > index + 1) { //多行
      val offset1 = layout.getPrimaryHorizontal(offsetIndexList[index])
      //这里还减去的数值是实验所得，待研究 todo
      val offset2 = layout.getPrimaryHorizontal(offsetIndexList[index + 1] - 1) - textSize / DimenUtil.dp2px(context, 1.275f)
      if (offset1 > offset2) offset2 else offset1 //取最小值是为了处理阿语环境下英文文字描边异常或英语环境下阿语文字描边异常的问题
    } else { //单行
      val offset1 = layout.getPrimaryHorizontal(offsetIndexList[index])
      val offset2 = layout.getPrimaryHorizontal(text.length)
      if (offset1 > offset2) offset2 else offset1 //取最小值是为了处理阿语环境下英文文字描边异常或英语环境下阿语文字描边异常的问题
    }
    return if (isRtlLayout(context)) {
      Pair(offset + getDrawableEndWidthAndPadding() + paddingEnd, top)
    } else {
      Pair(offset + getDrawableStartWidthAndPadding() + paddingStart, top)
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