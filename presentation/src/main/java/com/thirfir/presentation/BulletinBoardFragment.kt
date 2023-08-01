package com.thirfir.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.addQueryString
import com.thirfir.presentation.adapter.BulletinBoardAdapter
import com.thirfir.presentation.databinding.FragmentBulletinBoardBinding
import com.thirfir.presentation.model.BulletinBoard

class BulletinBoardFragment private constructor(): Fragment() {

    private lateinit var binding: FragmentBulletinBoardBinding

    private val bulletinBoards = listOf(
        BulletinBoard("일반공지", BASE_URL.addQueryString("b", 14)),
        BulletinBoard("장학공지", BASE_URL.addQueryString("b", 15)),
        BulletinBoard("학사공지", BASE_URL.addQueryString("b", 16)),
        BulletinBoard("학생생활", BASE_URL.addQueryString("b", 21)),
        BulletinBoard("채용공지", BASE_URL.addQueryString("b", 150)),
        BulletinBoard("현장실습공지", BASE_URL.addQueryString("b", 151)),
        BulletinBoard("사회봉사공지", BASE_URL.addQueryString("b", 191)),
        BulletinBoard("자유게시판", BASE_URL.addQueryString("b", 22)),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBulletinBoardBinding.inflate(layoutInflater, container, false)

        binding.recyclerViewBulletinBoard.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBulletinBoard.adapter = BulletinBoardAdapter(bulletinBoards)
        return binding.root
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