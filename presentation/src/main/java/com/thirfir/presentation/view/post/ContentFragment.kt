package com.thirfir.presentation.view.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BULLETIN
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.PID
import com.thirfir.domain.PID_QUERY
import com.thirfir.domain.util.addQueryString
import com.thirfir.presentation.adapter.AttachedFileAdapter
import com.thirfir.presentation.addViewOfTag
import com.thirfir.presentation.databinding.FragmentContentBinding
import com.thirfir.presentation.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentFragment: Fragment() {
    private lateinit var binding: FragmentContentBinding
    private val postViewModel: PostViewModel by activityViewModels()

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
                it.addViewOfTag(binding.contentContainer, null, requireContext())
            }

            binding.recyclerViewAttachedFile.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewAttachedFile.adapter = AttachedFileAdapter {
                val intent = Intent(requireActivity(), WebViewActivity::class.java)
                intent.putExtra("url", it.url)
                startActivity(intent)
            }.apply {
                submitList(post.attachedFiles)
            }
            binding.recyclerViewAttachedFile.addItemDecoration(
                object: RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: android.graphics.Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State,
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        outRect.bottom = 20
                    }
                }
            )
        }

        return binding.root
    }


    companion object {
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
        val intent = Intent(requireActivity(), WebViewActivity::class.java)
        intent.putExtra("url", BASE_URL.addQueryString(BULLETIN_QUERY, bulletin).addQueryString(
            PID_QUERY, pid))
        startActivity(intent)

        requireActivity().finish()
    }

}