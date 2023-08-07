package com.thirfir.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.presentation.adapter.PostAdapter
import com.thirfir.presentation.databinding.FragmentPostBinding
import com.thirfir.presentation.model.PostItem



class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postAdapter: PostAdapter
    private var currentPage = 1
    private val postsPerPage = 20

    val BASE_URL = "https://portal.koreatech.ac.kr/ctt/bb/bulletin"

    fun String.addQueryString(query: String, number: Int, postnum: Int): String {
        return if (this.contains("?"))
            "$this&$query=$number&ls=20&ln=1&dm=r&p=$postnum"
        else "$this?$query=$number&ls=20&ln=1&dm=r&p=$postnum"
    }

    private val postItems = listOf(
        PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)),
        PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)),
        // 추가 데이터 아이템들...
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)

        initClickListeners()
        initRecyclerView()
        initPaginationButtons()

        return binding.root
    }

    private fun initClickListeners() {
        postAdapter = PostAdapter { postItem ->
            val contentFragment = ContentFragment.newInstance(postItem.url)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, contentFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPost.adapter = postAdapter
        postAdapter.submitList(postItems)
    }

    private fun initPaginationButtons() {
        binding.prevButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                updateRecyclerViewData()
            }
        }

        binding.nextButton.setOnClickListener {
            val totalPage = (postItems.size + postsPerPage - 1) / postsPerPage
            if (currentPage < totalPage) {
                currentPage++
                updateRecyclerViewData()
            }
        }

        // 버튼 초기 상태 업데이트
        updateButtonState()
    }

    private fun updateRecyclerViewData() {
        val start = (currentPage - 1) * postsPerPage
        val end = minOf(start + postsPerPage, postItems.size)
        val sublist = postItems.subList(start, end)
        postAdapter.submitList(sublist)
        updateButtonState()
    }

    private fun updateButtonState() {
        val totalPage = (postItems.size + postsPerPage - 1) / postsPerPage
        binding.page1Button.isEnabled = currentPage != 1
        binding.page2Button.isEnabled = currentPage != 2
        binding.page3Button.isEnabled = currentPage != 3
        binding.page4Button.isEnabled = currentPage != 4
        binding.page5Button.isEnabled = currentPage != 5
        binding.prevButton.isEnabled = currentPage > 1
        binding.nextButton.isEnabled = currentPage < totalPage
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PostFragment().apply {
                arguments = Bundle().apply {
                    // Add any arguments if needed
                }
            }
    }
}