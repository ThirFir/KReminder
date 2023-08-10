package com.thirfir.presentation.view.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.FragmentPostBinding


class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    // private lateinit var binding: FragmentContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//아마 수정을 좀 많이 해야할지도..?
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val url = arguments?.getString(ARG_URL) ?: ""// 일단 url 받아오긴했는데..

        binding.postTitle.text="크롤링"// 게시글 제목 설정
        binding.postDetail.text="[크롤링]"
        binding.postWriter.text="작성자 |" + "크롤링"
        binding.postDate.text="날짜 |" + "크롤링"
        binding.postContent.text= "크롤링"
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    companion object {
        private const val ARG_URL = "arg_url"

        @JvmStatic
        fun newInstance(url: String) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
    }

}