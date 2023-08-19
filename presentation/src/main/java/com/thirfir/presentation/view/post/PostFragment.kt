package com.thirfir.presentation.view.post

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.PID
import com.thirfir.domain.model.PostHeader
import com.thirfir.presentation.databinding.FragmentPostBinding
import com.thirfir.presentation.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NullPointerException


@AndroidEntryPoint
class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding
    private val bulletin: Int by lazy {
        arguments?.getInt(BULLETIN_QUERY) ?: 0
    }
    private val pid: Int by lazy {
        arguments?.getInt(PID) ?: 0
    }
    private val postHeader: PostHeader by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("header", PostHeader::class.java) ?: throw NullPointerException("PostHeader is null")
        } else {
            arguments?.getParcelable("header") ?: throw NullPointerException("PostHeader is null")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(layoutInflater, container, false)

        binding.run {
            postTitle.text = postHeader.title
            binding.postDetail.text = "수정해야댐"
            binding.postWriter.text = postHeader.author
            binding.postDate.text = postHeader.date // TODO : Date Format
        }


        parentFragmentManager.beginTransaction()
            .add(binding.postContent.id, ContentFragment.newInstance(bulletin, pid))
            .commit()

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(bulletin: Int, pid: Int, postHeader: PostHeader) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putInt(BULLETIN_QUERY, bulletin)
                    putInt(PID, pid)
                    putParcelable("header", postHeader)
                }
            }
    }

}