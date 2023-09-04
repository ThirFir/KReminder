package com.thirfir.presentation.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.thirfir.presentation.databinding.FragmentBookmarksBinding
import com.thirfir.presentation.viewmodel.BookmarksViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarksFragment: Fragment() {
    private lateinit var binding: FragmentBookmarksBinding

    private val viewModel: BookmarksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentBookmarksBinding.inflate(layoutInflater,container,false)
        initView()

        return binding.root
    }

    private fun initView() {
        binding.viewModel = viewModel
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            BookmarksFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}