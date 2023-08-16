package com.thirfir.presentation.view.post

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.thirfir.domain.BACKGROUND
import com.thirfir.domain.BACKGROUND_COLOR
import com.thirfir.domain.BORDER
import com.thirfir.domain.BORDER_BOTTOM
import com.thirfir.domain.BORDER_COLOR
import com.thirfir.domain.BORDER_LEFT
import com.thirfir.domain.BORDER_RIGHT
import com.thirfir.domain.BORDER_STYLE
import com.thirfir.domain.BORDER_TOP
import com.thirfir.domain.BORDER_WIDTH
import com.thirfir.domain.HEIGHT
import com.thirfir.domain.NONE
import com.thirfir.domain.SOLID
import com.thirfir.domain.WIDTH
import com.thirfir.presentation.addViewOfTag
import com.thirfir.presentation.model.TableElement
import com.thirfir.presentation.toBorder
import com.thirfir.presentation.toColor
import com.thirfir.presentation.toDP
import com.thirfir.presentation.upscale
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class TableItemView(
    context: Context,
    private val tableElement: TableElement
) : LinearLayout(context) {

    private val rect = Rect()
    private val leftBorderPaint = Paint()
    private val rightBorderPaint = Paint()
    private val topBorderPaint = Paint()
    private val bottomBorderPaint = Paint()

    var mWidth = 0
    var mHeight = 0

    init {
        gravity = Gravity.CENTER
        orientation = VERTICAL
        setBackgroundColor(tableElement.styles[BACKGROUND].toColor(true))
        setPadding(20, 10, 20, 10)
        setSize()
        tableElement.childElements?.forEach {
            it.addViewOfTag(this, null, context)
        }
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setBorderPaint()
        drawBorder(canvas)
    }

    private fun setSize() {
        mWidth = if (tableElement.styles[WIDTH] == null)
            ViewGroup.LayoutParams.WRAP_CONTENT
        else tableElement.styles[WIDTH].toDP(context).roundToInt().upscale().upscale().upscale()
        mHeight = if (tableElement.styles[HEIGHT] == null)
            ViewGroup.LayoutParams.WRAP_CONTENT
        else tableElement.styles[HEIGHT].toDP(context).roundToInt().upscale().upscale().upscale()

        layoutParams = LayoutParams(mWidth, mHeight)
        rect.set(0, 0, mWidth, mHeight)
    }

    fun setFitSize() {
        layoutParams.width = mWidth
        layoutParams.height = mHeight
        rect.set(0, 0, mWidth, mHeight)
        invalidate()
    }

    private fun drawBorder(canvas: Canvas?) {
        if(leftBorderPaint.strokeWidth > 0f)
            canvas?.drawLine(0f, 0f, 0f, rect.height().toFloat(), leftBorderPaint)
        if(topBorderPaint.strokeWidth > 0f)
            canvas?.drawLine(0f, 0f, rect.width().toFloat(), 0f, topBorderPaint)
        if(bottomBorderPaint.strokeWidth > 0f)
            canvas?.drawLine(0f, rect.height().toFloat(), rect.width().toFloat(), rect.height().toFloat(), bottomBorderPaint)
        if(rightBorderPaint.strokeWidth > 0f)
            canvas?.drawLine(rect.width().toFloat(), 0f, rect.width().toFloat(), rect.height().toFloat(), rightBorderPaint)

    }

    private fun setBorderPaint() {
        leftBorderPaint.style = Paint.Style.STROKE
        rightBorderPaint.style = Paint.Style.STROKE
        topBorderPaint.style = Paint.Style.STROKE
        bottomBorderPaint.style = Paint.Style.STROKE

        if(tableElement.styles[BORDER] != null) {
            val border = tableElement.styles[BORDER]!!.toBorder()
            leftBorderPaint.color = border.color
            leftBorderPaint.strokeWidth = border.width.upscale()
            leftBorderPaint.style = Paint.Style.STROKE
            rightBorderPaint.color = border.color
            rightBorderPaint.strokeWidth = border.width.upscale()
            rightBorderPaint.style = Paint.Style.STROKE
            topBorderPaint.color = border.color
            topBorderPaint.strokeWidth = border.width.upscale()
            topBorderPaint.style = Paint.Style.STROKE
            bottomBorderPaint.color = border.color
            bottomBorderPaint.strokeWidth = border.width.upscale()
            bottomBorderPaint.style = Paint.Style.STROKE
        }
        if(tableElement.styles[BORDER_STYLE] == NONE) {
            leftBorderPaint.strokeWidth = 0f
            rightBorderPaint.strokeWidth = 0f
            topBorderPaint.strokeWidth = 0f
            bottomBorderPaint.strokeWidth = 0f
        }
        if(tableElement.styles[BORDER_COLOR] != null) {
            leftBorderPaint.color = tableElement.styles[BORDER_COLOR].toColor()
            rightBorderPaint.color = tableElement.styles[BORDER_COLOR].toColor()
            topBorderPaint.color = tableElement.styles[BORDER_COLOR].toColor()
            bottomBorderPaint.color = tableElement.styles[BORDER_COLOR].toColor()
        }
        if(tableElement.styles[BORDER_WIDTH] != null) {
            leftBorderPaint.strokeWidth = tableElement.styles[BORDER_WIDTH].toDP(context).upscale()
            rightBorderPaint.strokeWidth = tableElement.styles[BORDER_WIDTH].toDP(context).upscale()
            topBorderPaint.strokeWidth = tableElement.styles[BORDER_WIDTH].toDP(context).upscale()
            bottomBorderPaint.strokeWidth = tableElement.styles[BORDER_WIDTH].toDP(context).upscale()
        }
        if(tableElement.styles[BORDER_LEFT] != null) {
            val border = tableElement.styles[BORDER_LEFT]!!.toBorder()
            leftBorderPaint.color = border.color
            leftBorderPaint.strokeWidth = border.width.upscale()
            leftBorderPaint.setBorderStyle(border.style)
        }
        if(tableElement.styles[BORDER_RIGHT] != null) {
            val border = tableElement.styles[BORDER_RIGHT]!!.toBorder()
            rightBorderPaint.color = border.color
            rightBorderPaint.strokeWidth = border.width.upscale()
            rightBorderPaint.setBorderStyle(border.style)
        }
        if (tableElement.styles[BORDER_TOP] != null) {
            val border = tableElement.styles[BORDER_TOP]!!.toBorder()
            topBorderPaint.color = border.color
            topBorderPaint.strokeWidth = border.width.upscale()
            topBorderPaint.setBorderStyle(border.style)
        }
        if(tableElement.styles[BORDER_BOTTOM] != null) {
            val border = tableElement.styles[BORDER_BOTTOM]!!.toBorder()
            bottomBorderPaint.color = border.color
            bottomBorderPaint.strokeWidth = border.width.upscale()
            bottomBorderPaint.setBorderStyle(border.style)
        }
    }

    private fun Paint.setBorderStyle(style: String) {
        when(style) {
            NONE -> this.strokeWidth = 0f
            SOLID -> this.style = Paint.Style.STROKE
            else -> this.style = Paint.Style.STROKE
        }
    }
}