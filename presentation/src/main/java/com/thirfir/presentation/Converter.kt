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
import com.thirfir.domain.DASHED
import com.thirfir.domain.DOTTED
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
import com.thirfir.domain.SOLID
import com.thirfir.domain.START
import com.thirfir.domain.TRANSPARENT
import com.thirfir.domain.WHITE
import com.thirfir.domain.YELLOW
import com.thirfir.presentation.model.Border
import com.thirfir.presentation.model.Padding
import java.util.Locale
import kotlin.math.roundToInt

internal fun String?.toColor(isBackground: Boolean = false): Int {
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
internal fun String?.toTextStyle(): Int {
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

internal fun String?.toDP(context: Context): Float {
    return pxToDP(context, this.extractPxValue())
}


internal fun String?.toGravity(): Int {
    if (this == null) return Gravity.START
    return when (this.lowercase(Locale.ROOT)) {
        LEFT, START -> Gravity.START
        CENTER, JUSTIFY -> Gravity.CENTER
        RIGHT, END -> Gravity.END
        else -> Gravity.START
    }
}

// 상 우 하 좌 or 상 좌우 하 or 상하 좌우 or all
internal fun String?.toPadding(): Padding {
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

internal fun String?.toBorder(): Border {
    if (this == null) return Border(SOLID, 0f, Color.BLACK)

    val values = this.trim().split(' ')
    var width = 0f
    var style = ""
    var color = Color.BLACK

    for(value in values) {
        if(value.contains("pt")) {
            width = value.extractPxValue()
        } else if(value.contains(SOLID) || value.contains(DASHED) || value.contains(DOTTED)) {
            style = value
        } else {
            color = value.toColor()
        }
    }
    return Border(style, width, color)
}

internal fun String?.toBorderStyle(): List<String> {
    if (this == null) return List(4) { SOLID }

    val values = this.split(' ')
    return when(values.size) {
        1 -> List(4) { values[0] }
        2 -> listOf(values[0], values[1], values[0], values[1])
        3 -> listOf(values[0], values[1], values[2], values[1])
        else -> listOf(values[0], values[1], values[2], values[3])
    }
}

internal fun String?.toBorderWidth(): List<Float> {
    if (this == null) return List(4) { 0f }

    val values = this.split(' ')
    return when(values.size) {
        1 -> List(4) { values[0].extractPxValue() }
        2 -> listOf(values[0].extractPxValue(), values[1].extractPxValue(), values[0].extractPxValue(), values[1].extractPxValue())
        3 -> listOf(values[0].extractPxValue(), values[1].extractPxValue(), values[2].extractPxValue(), values[1].extractPxValue())
        else -> listOf(values[0].extractPxValue(), values[1].extractPxValue(), values[2].extractPxValue(), values[3].extractPxValue())
    }
}

internal fun String?.toBorderColor(): List<Int> {
    if (this == null) return List(4) { Color.BLACK }

    val values = this.split(' ')
    return when(values.size) {
        1 -> List(4) { values[0].toColor() }
        2 -> listOf(values[0].toColor(), values[1].toColor(), values[0].toColor(), values[1].toColor())
        3 -> listOf(values[0].toColor(), values[1].toColor(), values[2].toColor(), values[1].toColor())
        else -> listOf(values[0].toColor(), values[1].toColor(), values[2].toColor(), values[3].toColor())
    }
}

internal fun pxToDP(context: Context, px: Float): Float {
    val metrics = context.resources.displayMetrics
    return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

internal fun dpToSP(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density / context.resources.displayMetrics.scaledDensity
}
internal fun String?.extractPxValue(): Float {
    if (this == null) return 13f

    return when {
        this.endsWith("pt") -> this.replace("pt", "").toFloat() / 0.75f
        this.endsWith("px") -> this.replace("px", "").toFloat()
        this.endsWith("em") -> this.replace("em", "").toFloat() * 12 * 0.75f
        this.endsWith("%") -> this.replace("%", "").toFloat() / 100 * 12f
        else -> 13f
    }
}


private const val UPSCALE_RATE = 1.85f

internal fun Int.upscale(): Int {
    return (this * UPSCALE_RATE).roundToInt()
}
internal fun Float.upscale(): Float {
    return this * UPSCALE_RATE
}