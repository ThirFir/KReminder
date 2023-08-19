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
import android.util.Log
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
import com.thirfir.domain.COLSPAN
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
import com.thirfir.presentation.addViewOfTag
import com.thirfir.presentation.model.Padding
import com.thirfir.presentation.toColor
import com.thirfir.presentation.toGravity
import com.thirfir.presentation.toPadding
import com.thirfir.presentation.toTextStyle
import com.thirfir.presentation.databinding.FragmentContentBinding
import com.thirfir.presentation.upscale
import com.thirfir.presentation.extractPxValue
import com.thirfir.presentation.model.TableElement
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
                it.addViewOfTag(binding.root, null, requireContext())
            }
        }

        return binding.root
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