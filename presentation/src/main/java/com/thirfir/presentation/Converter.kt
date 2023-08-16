package com.thirfir.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.Gravity
import com.thirfir.domain.BLACK
import com.thirfir.domain.BLUE
import com.thirfir.domain.BOLD
import com.thirfir.domain.CENTER
import com.thirfir.domain.CYAN
import com.thirfir.domain.DARK_GRAY
import com.thirfir.domain.END
import com.thirfir.domain.GRAY
import com.thirfir.domain.GREEN
import com.thirfir.domain.ITALIC
import com.thirfir.domain.JUSTIFY
import com.thirfir.domain.LEFT
import com.thirfir.domain.LIGHT_GRAY
import com.thirfir.domain.MAGENTA
import com.thirfir.domain.RED
import com.thirfir.domain.RIGHT
import com.thirfir.domain.START
import com.thirfir.domain.TRANSPARENT
import com.thirfir.domain.WHITE
import com.thirfir.domain.YELLOW
import com.thirfir.domain.model.Padding
import java.util.Locale
import kotlin.math.roundToInt

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
        "400" -> Typeface.NORMAL
        "700" -> Typeface.BOLD
        BOLD -> Typeface.BOLD
        ITALIC -> Typeface.ITALIC
        "oblique" -> Typeface.BOLD_ITALIC
        else -> Typeface.NORMAL
    }
}

fun String?.toDP(context: Context): Float {
    return pxToDP(context, this.extractPxValue())
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

// 상 우 하 좌 or 상 좌우 하 or 상하 좌우 or all
fun String?.toPadding(context: Context): Padding {
    if (this == null) return Padding(0f, 0f, 0f, 0f)

    val values = this.split(' ')

    return when(values.size) {
        1 -> Padding(
            values[0].extractPxValue(),
            values[0].extractPxValue(),
            values[0].extractPxValue(),
            values[0].extractPxValue()
            )
        2 -> Padding(
            values[0].extractPxValue(),
            values[1].extractPxValue(),
            values[0].extractPxValue(),
            values[1].extractPxValue()
        )
        3 -> Padding(
            values[0].extractPxValue(),
            values[1].extractPxValue(),
            values[2].extractPxValue(),
            values[1].extractPxValue()
        )
        else -> Padding(
            values[0].extractPxValue(),
            values[1].extractPxValue(),
            values[2].extractPxValue(),
            values[3].extractPxValue()
        )
    }
}

fun pxToDP(context: Context, px: Float): Float {
    val metrics = context.resources.displayMetrics
    return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun dpToSP(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density / context.resources.displayMetrics.scaledDensity
}
fun String?.extractPxValue(): Float {
    if (this == null) return 13f

    return when {
        this.endsWith("pt") -> this.replace("pt", "").toFloat() / 0.75f
        this.endsWith("px") -> this.replace("px", "").toFloat()
        this.endsWith("em") -> this.replace("em", "").toFloat() * 12 * 0.75f
        this.endsWith("%") -> this.replace("%", "").toFloat() / 100 * 12f
        else -> 13f
    }
}

private const val UPSCALE_RATE = 1.75f

fun Int.upscale(): Int {
    return (this * UPSCALE_RATE).roundToInt()
}
fun Float.upscale(): Float {
    return this * UPSCALE_RATE
}