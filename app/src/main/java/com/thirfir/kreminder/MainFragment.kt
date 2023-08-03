package com.thirfir.kreminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.kreminder.databinding.FragmentMainBinding
import com.thirfir.kreminder.item.MainItem
import com.thirfir.domain.BASE_URL
import com.thirfir.kreminder.adapter.MainAdapter

class MainFragment private constructor(): Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var onMainBoardClickListener: (MainItem) -> Unit


    private val MainItems = listOf(
        MainItem("일반공지", BASE_URL.addQueryString("b", 14)),
        MainItem("장학공지", BASE_URL.addQueryString("b", 15)),
        MainItem("학사공지", BASE_URL.addQueryString("b", 16)),
        MainItem("학생생활", BASE_URL.addQueryString("b", 21)),
        MainItem("채용공지", BASE_URL.addQueryString("b", 150)),
        MainItem("현장실습공지", BASE_URL.addQueryString("b", 151)),
        MainItem("사회봉사공지", BASE_URL.addQueryString("b", 191)),
        MainItem("자유게시판", BASE_URL.addQueryString("b", 22)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        initClickListeners()
        initRecyclerView()


        return binding.root
    }


    fun initRecyclerView(){
        binding.recyclerViewMain.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerViewMain.adapter=MainAdapter(MainItems, onMainBoardClickListener)
   }

    private fun initClickListeners() {
        onMainBoardClickListener = {
            // TODO("START POST LIST ACTIVITY")
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}