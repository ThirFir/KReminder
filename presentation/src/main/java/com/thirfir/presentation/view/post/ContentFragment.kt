package com.thirfir.presentation.view.post

import android.content.Intent
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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.thirfir.domain.A
import com.thirfir.domain.B
import com.thirfir.domain.BACKGROUND_COLOR
import com.thirfir.domain.BOLD
import com.thirfir.domain.BR
import com.thirfir.domain.COLOR
import com.thirfir.domain.DEL
import com.thirfir.domain.DIV
import com.thirfir.domain.EM
import com.thirfir.domain.FONT_SIZE
import com.thirfir.domain.FONT_WEIGHT
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
import com.thirfir.domain.model.Padding
import com.thirfir.presentation.toColor
import com.thirfir.presentation.toGravity
import com.thirfir.presentation.toPadding
import com.thirfir.presentation.toTextStyle
import com.thirfir.presentation.databinding.FragmentContentBinding
import com.thirfir.presentation.upscale
import com.thirfir.presentation.extractPxValue
import com.thirfir.presentation.toDP
import com.thirfir.presentation.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class ContentFragment: Fragment() {
    private lateinit var binding: FragmentContentBinding
    private val postViewModel: PostViewModel by viewModels()

    private val bulletin: Int by lazy { arguments?.getInt(BULLETIN) ?: 0 }
    private val pid: Int by lazy { arguments?.getInt(PID) ?: 0 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postViewModel.registerExceptionCallback(object : PostViewModel.ExceptionCallback {
            override fun onException(e: Exception) {
                startWebView()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContentBinding.inflate(layoutInflater, container, false)
        postViewModel.fetchPost(bulletin, pid) { post ->
            post.htmlElements.forEach {
                addViewOfTag(it, binding.root, null)
            }
        }

        return binding.root
    }

    private fun addViewOfTag(element: HtmlElement, parentLayout: ViewGroup, textView: TextView?) {
        when(element.tag) {
            DIV -> {
                val linearLayout = getLinearLayout(element)
                val tv = getTextView(element)

                parentLayout.addView(linearLayout)
                linearLayout.addView(tv)
                element.childElements?.forEach {
                    addViewOfTag(it, linearLayout, tv)
                }
                if(tv.text.isEmpty())
                    linearLayout.removeView(tv)
                if(linearLayout.isEmpty())
                    parentLayout.removeView(linearLayout)
            }
            P -> {
                val tv = getTextView(element)
                parentLayout.addView(tv)
                element.childElements?.forEach {
                    addViewOfTag(it, parentLayout, tv)
                }
                if(tv.text.isEmpty())
                    parentLayout.removeView(tv)
            }
            SPAN -> {
                element.childElements?.forEach {
                    addViewOfTag(it, parentLayout, textView)
                }
            }
            B, STRONG -> {
                addStyleToChildren(element, FONT_WEIGHT, BOLD)
                element.childElements?.forEach {
                    addViewOfTag(it, parentLayout, textView)
                }
            }
            U, INS -> {
                addStyleToChildren(element, TEXT_DECORATION_LINE, UNDERLINE)
                element.childElements?.forEach {
                    addViewOfTag(it, parentLayout, textView)
                }
            }
            I, EM -> {
                addStyleToChildren(element, FONT_WEIGHT, ITALIC)
                element.childElements?.forEach {
                    addViewOfTag(it, parentLayout, textView)
                }
            }
            STRIKE, DEL -> {
                addStyleToChildren(element, TEXT_DECORATION_LINE, LINE_THROUGH)
                element.childElements?.forEach {
                    addViewOfTag(it, parentLayout, textView)
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
                val intent = Intent(requireActivity(), ImageActivity::class.java).apply {
                    putExtra(SRC, KOREATECH_PORTAL_URL + element.attributes[SRC])
                }
                ImageView(requireContext()).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setOnClickListener {
                        startActivity(intent)
                    }
                }.also {
                    parentLayout.addView(it)
                    Glide.with(it).load(KOREATECH_PORTAL_URL + element.attributes[SRC])
                        .into(it)
                }
            }
            TABLE -> {
                // TODO : Make Table
            }
            null -> {
                textView?.append(getSpannableStringBuilder(element.text!!, element.styles))
            }
        }
    }

    private fun addStyleToChildren(element: HtmlElement, key: String, value: String) {
        element.styles[key] = value
        element.childElements?.forEach {
            addStyleToChildren(it, key, value)
        }
    }

    private fun getSpannableStringBuilder(expandedText: String, styles: Map<String, String>) : SpannableStringBuilder {
        return SpannableStringBuilder().apply {
            append(getSpannableString(expandedText, styles))
        }
    }

    private fun getSpannableString(text: String, styles: Map<String, String>) : SpannableString {
        val flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        return SpannableString(text).apply {
            setSpan(ForegroundColorSpan(styles[COLOR].toColor()), 0, length, flags)
            setSpan(AbsoluteSizeSpan(styles[FONT_SIZE].toDP(requireContext()).upscale().roundToInt(), true),
                0, length, flags)
            setSpan(StyleSpan(styles[FONT_WEIGHT].toTextStyle()), 0, length, flags)
            setSpan(BackgroundColorSpan(styles[BACKGROUND_COLOR].toColor(isBackground = true)),
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
        val padding = styles[PADDING]?.toPadding(requireContext()) ?: Padding(0f, 0f, 0f, 0f)
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

    private fun getTextView(element: HtmlElement) = TextView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setTextSize(Dimension.SP, 14f)
        gravity = element.styles[TEXT_ALIGN].toGravity()
        setBackgroundColor(element.styles[BACKGROUND_COLOR].toColor(true))
        setText("", TextView.BufferType.SPANNABLE)
    }

    private fun getLinearLayout(element: HtmlElement) = LinearLayout(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        orientation = LinearLayout.VERTICAL
        gravity = element.styles[TEXT_ALIGN].toGravity()
        setPaddings(element.styles)
        setBackgroundColor(element.styles[BACKGROUND_COLOR].toColor(true))
    }


    companion object {
        private const val BULLETIN = "bulletin"
        private const val PID = "pid"
        @JvmStatic
        fun newInstance(bulletin: Int, pid: Int) =
            ContentFragment().apply {
                arguments = Bundle().apply {
                    putInt(BULLETIN, bulletin)
                    putInt(PID, pid)
                }
            }
    }

    private fun startWebView() {
        val intent = Intent(requireActivity(), PostWebViewActivity::class.java)
        intent.putExtra(BULLETIN, bulletin)
        intent.putExtra(PID, pid)
        startActivity(intent)

        requireActivity().finish()
    }
}