package com.thirfir.presentation.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.presentation.adapter.BulletinBoardsAdapter
import com.thirfir.presentation.databinding.FragmentBulletinBoardBinding
import com.thirfir.presentation.model.BulletinBoardItem
import com.thirfir.presentation.view.post.PostListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BulletinBoardFragment: Fragment() {

    private lateinit var binding: FragmentBulletinBoardBinding
    private lateinit var onBulletinBoardClickListener: (BulletinBoardItem) -> Unit

    private val bulletinBoardItems = listOf(
        BulletinBoardItem("일반공지", 14),
        BulletinBoardItem("장학공지", 15),
        BulletinBoardItem("학사공지", 16),
        BulletinBoardItem("학생생활", 21),
        BulletinBoardItem("채용공지", 150),
        BulletinBoardItem("현장실습공지", 151),
        BulletinBoardItem("사회봉사공지", 191),
        BulletinBoardItem("자유게시판", 22),
    )

    private val modalBottomSheet : BottomSheetDialogFragment by lazy {
        OverflowMenuModalBottomSheet()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBulletinBoardBinding.inflate(layoutInflater, container, false)

        initClickListeners()
        initRecyclerView()

        return binding.root
    }

    private fun initClickListeners() {
        onBulletinBoardClickListener = {
            val intent = Intent(requireContext(), PostListActivity::class.java).apply {
                putExtra(BULLETIN_QUERY, it.bulletin)
            }
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewBulletinBoard.also {
            it.adapter = BulletinBoardsAdapter(bulletinBoardItems, onBulletinBoardClickListener)
            it.layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.addItemDecoration(
                DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BulletinBoardFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}