package com.thirfir.domain

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import java.util.Locale

fun String?.toColor(isBackground: Boolean = false): Int {
    if (this == null)
        return if (isBackground) Color.TRANSPARENT else Color.BLACK
    if (contains("rgb", true)) {
        val rgb = this.substringAfter("rgb(").substringBefore(")").split(",")
        return Color.rgb(rgb[0].trim().toInt(), rgb[1].trim().toInt(), rgb[2].trim().toInt())
    }
    if (contains("#", true)) {
        return Color.parseColor(this)
    }
    return when (this.trim().lowercase(Locale.ROOT)) {
        BLACK -> Color.BLACK
        BLUE -> Color.BLUE
        CYAN -> Color.CYAN
        DARK_GRAY -> Color.DKGRAY
        GRAY -> Color.GRAY
        GREEN -> Color.GREEN
        LIGHT_GRAY -> Color.LTGRAY
        MAGENTA -> Color.MAGENTA
        RED -> Color.RED
        WHITE -> Color.WHITE
        YELLOW -> Color.YELLOW
        TRANSPARENT -> Color.TRANSPARENT
        else -> Color.BLACK
    }
}

/** font-weight to textStyle */
fun String?.toTextStyle(): Int {
    if (this == null) return Typeface.NORMAL
    return when (this.lowercase(Locale.ROOT)) {
        BOLD -> Typeface.BOLD
        ITALIC -> Typeface.ITALIC
        "oblique" -> Typeface.BOLD_ITALIC
        else -> Typeface.NORMAL
    }
}

fun String?.toDP(context: Context): Float {
    if (this == null) return 50f
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    val fontSizeRegex = Regex("[0-9]+\\.?[0-9]*") // 정규식으로 크기 추출
    val sizeValue = fontSizeRegex.find(this)?.value?.toFloatOrNull()

    val pt = sizeValue?.let { pt ->
        pt * (displayMetrics.density / 1.33f)
    } ?: 50f
    return when {
        this.endsWith("pt") -> pt
        this.endsWith("px") -> pt / 0.75f
        this.endsWith("em") -> pt / 12f
        this.endsWith("%") -> pt * 100 / 12
        else -> pt
    }
}

fun String?.toGravity(): Int {
    if (this == null) return Gravity.START
    return when (this.lowercase(Locale.ROOT)) {
        LEFT, START -> Gravity.START
        CENTER, JUSTIFY -> Gravity.CENTER
        RIGHT, END -> Gravity.END
        else -> Gravity.START
    }
}
