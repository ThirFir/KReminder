package com.thirfir.presentation.view.keyword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.presentation.adapter.KeywordsAdapter
import com.thirfir.presentation.databinding.FragmentKeywordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeywordFragment: Fragment() {
    private lateinit var binding: FragmentKeywordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKeywordBinding.inflate(layoutInflater, container, false)
        initClickListeners()
        initRecyclerView()


        return binding.root
    }

    private fun initClickListeners() {

    }

    private fun initRecyclerView() {
        binding.recyclerViewKeyword.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewKeyword.adapter = KeywordsAdapter(listOf()) {
            // TODO : KeywordViewModel
            // keywordViewModel.deleteKeyword(it)
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() =
            KeywordFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}