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
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
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
import com.thirfir.domain.model.element.TextElement
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
        postViewModel.fetchPost(bulletin, pid) {
            it.parentElements.forEach { parentElement ->
                addViewOfTag(parentElement)
            }
        }

        return binding.root
    }

    private fun addViewOfTag(parentElement: ParentElement) {
        when(parentElement.enabledRootTag) {
            EnabledRootTag.P -> {
                addTextView(parentElement.textElements)
            }
            EnabledRootTag.TABLE -> {
                binding.root.addView(TableView(requireContext(), parentElement.tables!!))
            }
            EnabledRootTag.H3 -> {

            }
            EnabledRootTag.DIV -> {

            }
        }
    }

    private fun addTextView(textElements: List<TextElement>) {
        if(textElements.isNotEmpty()) {
            binding.root.addView(getStyledTextView(textElements))
        }
    }

    private fun getStyledTextView(textElements: List<TextElement>) : TextView {
        var textView = TextView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        var gravity: Int = Gravity.START

        val builder = SpannableStringBuilder()
        textElements.forEach {
            builder.append(getSpannableString(it))
            gravity = it.style[TEXT_ALIGN].toGravity()
        }

        // TODO : Background Color 전체 or 텍스트만
        textView.text = builder
        textView.gravity = gravity
        return textView
    }

    private fun getSpannableString(te: TextElement) : SpannableString {
        val flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        return SpannableString(te.text).apply {
            setSpan(ForegroundColorSpan(te.style[COLOR].toColor()), 0, length, flags)
            setSpan(AbsoluteSizeSpan(te.style[FONT_SIZE]?.toDP(requireContext())?.roundToInt() ?: 100), 0, length, flags)
            setSpan(StyleSpan(te.style[FONT_WEIGHT].toTextStyle()), 0, length, flags)
            setSpan(BackgroundColorSpan(te.style[BACKGROUND_COLOR].toColor(isBackground = true)), 0, length, flags)
            if(te.style[TEXT_DECORATION_LINE] == UNDERLINE) {
                setSpan(UnderlineSpan(), 0, length, flags)
            }
            if(te.style[TEXT_DECORATION_LINE] == LINE_THROUGH) {
                setSpan(StrikethroughSpan(), 0, length, flags)
            }
        }
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