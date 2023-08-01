package com.thirfir.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.addQueryString
import com.thirfir.presentation.adapter.BulletinBoardsAdapter
import com.thirfir.presentation.databinding.FragmentBulletinBoardBinding
import com.thirfir.presentation.model.BulletinBoardItem

class BulletinBoardFragment private constructor(): Fragment() {

    private lateinit var binding: FragmentBulletinBoardBinding
    private lateinit var onBulletinBoardClickListener: (BulletinBoardItem) -> Unit

    /* 이게 맞나? */
    private val bulletinBoardItems = listOf(
        BulletinBoardItem("일반공지", BASE_URL.addQueryString("b", 14)),
        BulletinBoardItem("장학공지", BASE_URL.addQueryString("b", 15)),
        BulletinBoardItem("학사공지", BASE_URL.addQueryString("b", 16)),
        BulletinBoardItem("학생생활", BASE_URL.addQueryString("b", 21)),
        BulletinBoardItem("채용공지", BASE_URL.addQueryString("b", 150)),
        BulletinBoardItem("현장실습공지", BASE_URL.addQueryString("b", 151)),
        BulletinBoardItem("사회봉사공지", BASE_URL.addQueryString("b", 191)),
        BulletinBoardItem("자유게시판", BASE_URL.addQueryString("b", 22)),
    )
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
            // TODO("START POST LIST ACTIVITY")
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewBulletinBoard.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBulletinBoard.adapter = BulletinBoardsAdapter(bulletinBoardItems, onBulletinBoardClickListener)
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