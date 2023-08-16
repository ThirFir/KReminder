package com.thirfir.presentation

import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.view.isEmpty
import com.bumptech.glide.Glide
import com.thirfir.domain.A
import com.thirfir.domain.B
import com.thirfir.domain.BACKGROUND_COLOR
import com.thirfir.domain.BOLD
import com.thirfir.domain.BR
import com.thirfir.domain.COLOR
import com.thirfir.domain.COLSPAN
import com.thirfir.domain.DEL
import com.thirfir.domain.DIV
import com.thirfir.domain.EM
import com.thirfir.domain.FONT_SIZE
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.H3
import com.thirfir.domain.I
import com.thirfir.domain.IMG
import com.thirfir.domain.INS
import com.thirfir.domain.ITALIC
import com.thirfir.domain.KOREATECH_PORTAL_URL
import com.thirfir.domain.LINE_THROUGH
import com.thirfir.domain.P
import com.thirfir.domain.PADDING
import com.thirfir.domain.PADDING_BOTTOM
import com.thirfir.domain.PADDING_LEFT
import com.thirfir.domain.PADDING_RIGHT
import com.thirfir.domain.PADDING_TOP
import com.thirfir.domain.ROWSPAN
import com.thirfir.domain.SPAN
import com.thirfir.domain.SRC
import com.thirfir.domain.STRIKE
import com.thirfir.domain.STRONG
import com.thirfir.domain.TABLE
import com.thirfir.domain.TEXT_ALIGN
import com.thirfir.domain.TEXT_DECORATION_LINE
import com.thirfir.domain.U
import com.thirfir.domain.UNDERLINE
import com.thirfir.domain.model.HtmlElement
import com.thirfir.presentation.model.Padding
import com.thirfir.presentation.model.TableElement
import com.thirfir.presentation.view.post.ImageActivity
import com.thirfir.presentation.view.post.TableView
import kotlin.math.roundToInt

internal fun HtmlElement.addViewOfTag(parentLayout: ViewGroup, textView: TextView?, context: Context) {
    when(tag) {
        DIV -> {
            val linearLayout = getLinearLayout(context)
            val tv = getTextView(context)

            parentLayout.addView(linearLayout)
            linearLayout.addView(tv)
            childElements?.forEach {
                it.addViewOfTag(linearLayout, tv, context)
            }
            if(tv.text.isEmpty())
                linearLayout.removeView(tv)
            if(linearLayout.isEmpty())
                parentLayout.removeView(linearLayout)
        }
        P -> {
            val tv = getTextView(context)
            parentLayout.addView(tv)
            childElements?.forEach {
                it.addViewOfTag(parentLayout, tv, context)
            }
            if(tv.text.isEmpty())
                parentLayout.removeView(tv)
        }
        H3 -> {
            addStyleToChildren(FONT_WEIGHT, BOLD)
            val tv = getTextView(context)
            parentLayout.addView(tv)
            childElements?.forEach {
                it.addViewOfTag(parentLayout, tv, context)
            }
            if(tv.text.isEmpty())
                parentLayout.removeView(tv)
        }
        SPAN -> {
            childElements?.forEach {
                it.addViewOfTag(parentLayout, textView, context)
            }
        }
        B, STRONG -> {
            addStyleToChildren(FONT_WEIGHT, BOLD)
            childElements?.forEach {
                it.addViewOfTag(parentLayout, textView, context)
            }
        }
        U, INS -> {
            addStyleToChildren(TEXT_DECORATION_LINE, UNDERLINE)
            childElements?.forEach {
                it.addViewOfTag(parentLayout, textView, context)
            }
        }
        I, EM -> {
            addStyleToChildren(FONT_WEIGHT, ITALIC)
            childElements?.forEach {
                it.addViewOfTag(parentLayout, textView, context)
            }
        }
        STRIKE, DEL -> {
            addStyleToChildren(TEXT_DECORATION_LINE, LINE_THROUGH)
            childElements?.forEach {
                it.addViewOfTag(parentLayout, textView, context)
            }
        }
        BR -> {
            if(textView?.text?.isEmpty() == true)
                textView.append(" ")
            else
                textView?.append("\n")
        }
        A -> {
            // TODO : 링크
        }
        IMG -> {
            val intent = Intent(context, ImageActivity::class.java).apply {
                putExtra(SRC, KOREATECH_PORTAL_URL + attributes[SRC])
            }
            ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setOnClickListener {
                    context.startActivity(intent)
                }
            }.also {
                parentLayout.addView(it)
                Glide.with(it).load(KOREATECH_PORTAL_URL + attributes[SRC])
                    .into(it)
            }
        }
        TABLE -> {
            // TODO : Make Table
            childElements?.forEach {
                parentLayout.addView(HorizontalScrollView(context).apply {
                    this.addView(TableView(context, it.makeTable()))
                })
            }
        }
        null -> {
            textView?.append(getSpannableStringBuilder(context))
        }
    }
}

private fun HtmlElement.makeTable(): Array<Array<TableElement?>> {
    var rowSize = 0
    var columnSize = 0
    childElements?.forEach {
        columnSize = columnSize.coerceAtLeast(it.getColumnSize())
        ++rowSize
    }
    val table = Array(rowSize) { Array<TableElement?>(columnSize) { null } }
    val tableItemEnabled = Array(rowSize) { Array(columnSize) { true } }

    var rowIndex = 0
    childElements?.forEach { trElement ->
        var columnIndex = 0
        trElement.childElements?.forEach { tdElement ->
            while(!tableItemEnabled[rowIndex][columnIndex]) {
                ++columnIndex
            }
            if(tableItemEnabled[rowIndex][columnIndex]) {
                var rowSpan = tdElement.attributes[ROWSPAN]?.trim()?.toInt() ?: 1
                var colSpan = tdElement.attributes[COLSPAN]?.trim()?.toInt() ?: 1
                table[rowIndex][columnIndex] = TableElement(
                    rowSpan,
                    colSpan,
                    tdElement.childElements,
                    tdElement.styles
                )
                while (rowSpan > 1) {
                    tableItemEnabled[rowIndex + rowSpan - 1][columnIndex] = false
                    --rowSpan
                }
                while (colSpan > 1) {
                    tableItemEnabled[rowIndex][columnIndex + colSpan - 1] = false
                    --colSpan
                }
            }
            ++columnIndex
        }
        ++rowIndex
    }
    return table
}

private fun HtmlElement.getColumnSize(): Int {
    var rowSize = 0
    childElements?.forEach { _ ->
        ++rowSize
    }
    return rowSize
}

private fun HtmlElement.addStyleToChildren(key: String, value: String) {
    if(styles[key] == null)
        styles[key] = value
    childElements?.forEach {
        it.addStyleToChildren(key, value)
    }
}

private fun HtmlElement.getSpannableStringBuilder(context: Context) : SpannableStringBuilder {
    return SpannableStringBuilder().apply {
        append(getSpannableString(context))
    }
}

private fun HtmlElement.getSpannableString(context: Context) : SpannableString {
    val flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    return SpannableString(text).apply {
        setSpan(ForegroundColorSpan(styles[COLOR].toColor()), 0, length, flags)
        setSpan(
            AbsoluteSizeSpan(styles[FONT_SIZE].toDP(context).upscale().roundToInt(), true),
            0, length, flags)
        setSpan(StyleSpan(styles[FONT_WEIGHT].toTextStyle()), 0, length, flags)
        setSpan(
            BackgroundColorSpan(styles[BACKGROUND_COLOR].toColor(isBackground = true)),
            0, length, flags)
        if(styles[TEXT_DECORATION_LINE] == UNDERLINE) {
            setSpan(UnderlineSpan(), 0, length, flags)
        }
        if(styles[TEXT_DECORATION_LINE] == LINE_THROUGH) {
            setSpan(StrikethroughSpan(), 0, length, flags)
        }
    }
}

private fun View.setPaddings(styles: Map<String, String>) {
    val padding = styles[PADDING]?.toPadding() ?: Padding(0f, 0f, 0f, 0f)
    setPadding(
        padding.left.upscale().roundToInt(),
        padding.top.upscale().roundToInt(),
        padding.right.upscale().roundToInt(),
        padding.bottom.upscale().roundToInt()
    )

    val left = styles[PADDING_LEFT] ?: padding.left.toString()
    val top = styles[PADDING_TOP] ?: padding.top.toString()
    val right = styles[PADDING_RIGHT] ?: padding.right.toString()
    val bottom = styles[PADDING_BOTTOM] ?: padding.bottom.toString()
    setPadding(
        left.extractPxValue().upscale().roundToInt(),
        top.extractPxValue().upscale().roundToInt(),
        right.extractPxValue().upscale().roundToInt(),
        bottom.extractPxValue().upscale().roundToInt()
    )
}

private fun HtmlElement.getTextView(context: Context) = TextView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    setTextIsSelectable(true)
    setTextSize(Dimension.SP, 14f)
    gravity = styles[TEXT_ALIGN].toGravity()
    setBackgroundColor(styles[BACKGROUND_COLOR].toColor(true))
    setText("", TextView.BufferType.SPANNABLE)
}

private fun HtmlElement.getLinearLayout(context: Context) = LinearLayout(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    orientation = LinearLayout.VERTICAL
    gravity = styles[TEXT_ALIGN].toGravity()
    setPaddings(styles)
    setBackgroundColor(styles[BACKGROUND_COLOR].toColor(true))
}
