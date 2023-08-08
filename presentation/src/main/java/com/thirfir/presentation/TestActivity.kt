package com.thirfir.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import android.view.Gravity
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.activity.viewModels
import com.thirfir.domain.BACKGROUND_COLOR
import com.thirfir.domain.COLOR
import com.thirfir.domain.FONT_SIZE
import com.thirfir.domain.FONT_WEIGHT
import com.thirfir.domain.LINE_THROUGH
import com.thirfir.domain.TEXT_ALIGN
import com.thirfir.domain.TEXT_DECORATION_LINE
import com.thirfir.domain.UNDERLINE
import com.thirfir.domain.model.element.EnabledRootTag
import com.thirfir.domain.model.element.ParentElement
import com.thirfir.domain.toColor
import com.thirfir.domain.toDP
import com.thirfir.domain.toGravity
import com.thirfir.domain.toTextStyle
import com.thirfir.presentation.custom.TableView
import com.thirfir.presentation.databinding.ActivityTestBinding
import com.thirfir.presentation.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text
import kotlin.math.roundToInt


@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    val binding: ActivityTestBinding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }
    val postViewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        postViewModel.fetchPost(14, 30975) {
            it.parentElements.forEach {
                if(it.enabledRootTag == EnabledRootTag.TABLE)
                    for(r in 0 until it.tables!!.size) {
                        for(c in 0 until it.tables!![r].size) {
                            Log.d("dddddddd", it.tables!![r][c]?.rowSpan.toString() + " " + it.tables!![r][c]?.textElement.toString())
                        }
                    }
                //Log.d("test", it.textElements.toString())
            }
            it.parentElements.forEach { parent ->
                when(parent.enabledRootTag) {
                    EnabledRootTag.P -> {
                        whenP(parent)
                    }
                    EnabledRootTag.TABLE -> {
                        whenTable(parent)
                    }
                    EnabledRootTag.H3 -> {

                    }
                }
            }
        }

    }

    private fun whenP(parent: ParentElement) {
        binding.linear.addView(
            initText(parent)
            //tv
        )
    }

    private fun whenTable(parent: ParentElement) {
        binding.linear.addView(
            HorizontalScrollView(this).apply {
                addView(
                    TableView(this@TestActivity, parent.tables?.toList() ?: return)
                )
            }
        )
    }

    private fun whenH3(parent: ParentElement) {

    }

    fun initText(parent: ParentElement): TextView {
        var textView = TextView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        var gravity: Int = Gravity.START
        val builder = SpannableStringBuilder()
        parent.textElements.forEach {
            SpannableString(it.text).apply {
                setSpan(
                    ForegroundColorSpan(it.style[COLOR].toColor()), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    AbsoluteSizeSpan(it.style[FONT_SIZE]?.toDP(this@TestActivity)?.roundToInt() ?: 100), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if(it.style[TEXT_DECORATION_LINE] == UNDERLINE) {
                    setSpan(
                        UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                if(it.style[TEXT_DECORATION_LINE] == LINE_THROUGH) {
                    setSpan(
                        StrikethroughSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                setSpan(
                    StyleSpan(it.style[FONT_WEIGHT].toTextStyle()), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    BackgroundColorSpan(it.style[BACKGROUND_COLOR].toColor(isBackground = true)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.append(this)
                gravity = it.style[TEXT_ALIGN].toGravity()
            }
        }
        textView.text = builder
        return textView.apply { this.gravity = gravity }
    }
}