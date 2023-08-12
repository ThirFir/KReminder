package com.thirfir.presentation.view.post

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Spannable
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.thirfir.domain.BACKGROUND_COLOR
import com.thirfir.domain.COLOR
import com.thirfir.domain.FONT_SIZE
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.LINE_THROUGH
import com.thirfir.domain.TEXT_DECORATION_LINE
import com.thirfir.domain.UNDERLINE
import com.thirfir.domain.toColor
import com.thirfir.domain.model.element.TextElement
import com.thirfir.domain.toDP
import com.thirfir.domain.toTextStyle
import kotlin.math.roundToInt

class TableSpace(
    context: Context,
    private val textElements: List<TextElement>,
) : AppCompatTextView(context) {

    private val paint = Paint()
    private val rect = Rect()
    var mWidth = 0
    var mHeight = 0

    init {
        paint.color = Color.BLACK
        paint.textSize = 100f
        paint.strokeWidth = 1f
        paint.style = Paint.Style.STROKE

        gravity = Gravity.CENTER
        initText()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mWidth, mHeight)
        rect.set(0, 0, mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(rect, paint)
    }

    fun initText() {
        textElements.forEach {
            val spannable = Spannable.Factory.getInstance().newSpannable(it.text)
            spannable.setSpan(ForegroundColorSpan(it.style[COLOR].toColor()), 0, it.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)    // text color
            spannable.setSpan(AbsoluteSizeSpan(it.style[FONT_SIZE]?.toDP(context)?.roundToInt() ?: 50), 0, it.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)   // font-size
            if(it.style[TEXT_DECORATION_LINE] == UNDERLINE) {
                spannable.setSpan(UnderlineSpan(), 0, it.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)   // text-decoration-line : underline
            }
            if(it.style[TEXT_DECORATION_LINE] == LINE_THROUGH) {
                spannable.setSpan(StrikethroughSpan(), 0, it.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                // TODO line-through
            }
            spannable.setSpan(StyleSpan(it.style[FONT_WEIGHT].toTextStyle()), 0, it.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)    // font-weight : bold, italic, normal ...
            spannable.setSpan(BackgroundColorSpan(it.style[BACKGROUND_COLOR].toColor(isBackground = true)), 0, it.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)   // background-color
            append(spannable)
        }
    }

}