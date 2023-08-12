package com.thirfir.presentation.view.post

import android.annotation.SuppressLint
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
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
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
import com.thirfir.domain.INS
import com.thirfir.domain.ITALIC
import com.thirfir.domain.LINE_THROUGH
import com.thirfir.domain.P
import com.thirfir.domain.PADDING
import com.thirfir.domain.PADDING_BOTTOM
import com.thirfir.domain.PADDING_LEFT
import com.thirfir.domain.PADDING_RIGHT
import com.thirfir.domain.PADDING_TOP
import com.thirfir.domain.SPAN
import com.thirfir.domain.STRIKE
import com.thirfir.domain.STRONG
import com.thirfir.domain.TEXT_ALIGN
import com.thirfir.domain.TEXT_DECORATION_LINE
import com.thirfir.domain.U
import com.thirfir.domain.UNDERLINE
import com.thirfir.domain.model.HtmlElement
import com.thirfir.domain.toColor
import com.thirfir.domain.toDP
import com.thirfir.domain.toGravity
import com.thirfir.domain.toTextStyle
import com.thirfir.presentation.databinding.FragmentContentBinding
import com.thirfir.presentation.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class ContentFragment private constructor(): Fragment() {
    private lateinit var binding: FragmentContentBinding
    private val postViewModel: PostViewModel by viewModels()

    private val bulletin: Int by lazy { arguments?.getInt(BULLETIN) ?: 0 }
    private val pid: Int by lazy { arguments?.getInt(PID) ?: 0 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postViewModel.registerExceptionCallback(object : PostViewModel.ExceptionCallback {
            override fun onException(e: Exception) {
                val intent = Intent(requireActivity(), PostWebViewActivity::class.java)
                intent.putExtra(BULLETIN, bulletin)
                intent.putExtra(PID, pid)
                startActivity(intent)

                requireActivity().finish()
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
                addViewOfTag(it, binding.llContent)
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun addViewOfTag(element: HtmlElement, parentView: View) {
        when(element.tag) {
            DIV -> {
                val linearLayout = LinearLayout(requireContext()).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    orientation = LinearLayout.VERTICAL
                    gravity = element.styles[TEXT_ALIGN].toGravity()
                    setPaddings(element.styles)
                    setBackgroundColor(element.styles[BACKGROUND_COLOR].toColor(true))
                }
                (parentView as ViewGroup).addView(linearLayout)
                element.childElements?.forEach {
                    addViewOfTag(it, linearLayout)
                }
            }
            P -> {
                val textView = TextView(requireContext()).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    gravity = element.styles[TEXT_ALIGN].toGravity()
                    setBackgroundColor(element.styles[BACKGROUND_COLOR].toColor(true))
                    setText("", TextView.BufferType.SPANNABLE)
                }
                (parentView as ViewGroup).addView(textView)
                element.childElements?.forEach {
                    addViewOfTag(it, textView)
                }
            }
            SPAN -> {
                element.childElements?.forEach {
                    addViewOfTag(it, parentView)
                }
            }
            B, STRONG -> {
                element.childElements?.forEach {
                    addStyleToChildren(it, FONT_WEIGHT, BOLD)
                    addViewOfTag(it, parentView)
                }
            }
            U, INS -> {
                element.childElements?.forEach {
                    addStyleToChildren(it, TEXT_DECORATION_LINE, UNDERLINE)
                    addViewOfTag(it, parentView)
                }
            }
            I, EM -> {
                element.childElements?.forEach {
                    addStyleToChildren(it, FONT_WEIGHT, ITALIC)
                    addViewOfTag(it, parentView)
                }
            }
            STRIKE, DEL -> {
                element.childElements?.forEach {
                    addStyleToChildren(it, TEXT_DECORATION_LINE, LINE_THROUGH)
                    addViewOfTag(it, parentView)
                }
            }
            BR -> {
                when(parentView) {
                    is ViewGroup -> {
                        val textView = TextView(requireContext()).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            setBackgroundColor(element.styles[BACKGROUND_COLOR].toColor(true))
                            text = "\n"
                        }
                        parentView.addView(textView)
                    }
                    is TextView -> {
                        parentView.text = parentView.text.toString() + "\n"
                    }
                }
            }
            null -> {
                //Log.d("ContentFragment", element.text + element.styles.toString())
                when(parentView) {
                    is ViewGroup -> {
                        TextView(requireContext()).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            setText(getSpannableStringBuilder(element.text!!, element.styles), TextView.BufferType.SPANNABLE)
                            setBackgroundColor(element.styles[BACKGROUND_COLOR].toColor(true))
                            parentView.addView(this)
                        }
                    }
                    is TextView -> {
                        parentView.append(getSpannableStringBuilder(element.text!!, element.styles))
                    }
                }
            }
        }
    }

    private fun addStyleToChildren(element: HtmlElement, key: String, value: String) {
        Log.d("ContentFragment", element.toString())
        element.styles[key] = value
        element.childElements?.forEach {
            addStyleToChildren(it, key, value)
        }
    }

    private fun View.addTextView(element: HtmlElement) {
        val textView = TextView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            gravity = element.styles[TEXT_ALIGN].toGravity()
            setBackgroundColor(element.styles[BACKGROUND_COLOR].toColor(true))
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
            setSpan(AbsoluteSizeSpan(styles[FONT_SIZE].toDP(requireContext()).roundToInt()), 0, length, flags)
            setSpan(StyleSpan(styles[FONT_WEIGHT].toTextStyle()), 0, length, flags)
            setSpan(BackgroundColorSpan(styles[BACKGROUND_COLOR].toColor(isBackground = true)), 0, length, flags)
            if(styles[TEXT_DECORATION_LINE] == UNDERLINE) {
                setSpan(UnderlineSpan(), 0, length, flags)
            }
            if(styles[TEXT_DECORATION_LINE] == LINE_THROUGH) {
                setSpan(StrikethroughSpan(), 0, length, flags)
            }
        }
    }

    private fun View.setPaddings(styles: Map<String, String>) {
        setPadding(styles[PADDING].toDP(requireContext()).roundToInt())
        setPadding(
            styles[PADDING_TOP].toDP(requireContext()).roundToInt(),
            styles[PADDING_RIGHT].toDP(requireContext()).roundToInt(),
            styles[PADDING_BOTTOM].toDP(requireContext()).roundToInt(),
            styles[PADDING_LEFT].toDP(requireContext()).roundToInt()
        )
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
}