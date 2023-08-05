package com.thirfir.kreminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.kreminder.adapter.PostAdapter
import com.thirfir.kreminder.databinding.FragmentPostBinding
import com.thirfir.kreminder.item.PostItem


class PostFragment private constructor(): Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var onPostClickListener: (PostItem) -> Unit

    val BASE_URL = "https://portal.koreatech.ac.kr/ctt/bb/bulletin"//임시로 여기다 놓음.
    fun String.addQueryString(query: String, number: Int, postnum: Int): String {
        return if (this.contains("?"))
            "$this&$query=$number&ls=20&ln=1&dm=r&p=$postnum"
        else "$this?$query=$number&ls=20&ln=1&dm=r&p=$postnum"
    }


    private val PostItems = listOf(
        PostItem(30976,"일반공지", BASE_URL.addQueryString("b", 14,30976)),
        PostItem(30978,"장학공지", BASE_URL.addQueryString("b", 14,30978)),
    //크롤링한 데이터를 여기에 긁어와야될듯..? 아닌가..
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPostBinding.inflate(layoutInflater, container, false)

        initClickListeners()
        initRecyclerView()

        return binding.root
    }

    private fun initClickListeners() {
        onPostClickListener = {//아이템 선택시 해당 url을 content페이지로 넘겨줘서 현재 어느 곳에 접속해있는지를 알려줌. 흠..
                postItem ->
            // START POST LIST ACTIVITY (Replace with the code to navigate to content_fragment)
            val contentFragment = ContentFragment.newInstance(postItem.url)//postItem.title..???
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, contentFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPost.adapter = PostAdapter(PostItems, onPostClickListener)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PostFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}