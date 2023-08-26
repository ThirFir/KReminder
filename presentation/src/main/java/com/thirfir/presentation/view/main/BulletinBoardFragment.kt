package com.thirfir.presentation.view.main

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thirfir.domain.B
import com.thirfir.domain.BASE_URL
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.util.addQueryString
import com.thirfir.presentation.R
import com.thirfir.presentation.adapter.BulletinBoardsAdapter
import com.thirfir.presentation.databinding.FragmentBulletinBoardBinding
import com.thirfir.presentation.model.BulletinBoardItem
import com.thirfir.presentation.view.post.PostListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BulletinBoardFragment: Fragment() {

    private lateinit var binding: FragmentBulletinBoardBinding
    private lateinit var onBulletinBoardClickListener: (BulletinBoardItem) -> Unit

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
            it.onClick(requireContext())
        }
        binding.topAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.overflow_menu -> {
                    modalBottomSheet.show(requireActivity().supportFragmentManager,
                        OverflowMenuModalBottomSheet.TAG
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewBulletinBoard.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.recyclerViewBulletinBoard.adapter = BulletinBoardsAdapter(BulletinBoardItem.values().toList(), onBulletinBoardClickListener)
        binding.recyclerViewBulletinBoard.addItemDecoration(
            object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State,
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val position = parent.getChildAdapterPosition(view)
                    val count = state.itemCount
                    val spacing = 80

                    outRect.top = spacing
                    if(position == count - 1)
                        outRect.bottom = spacing
                }
            }
        )
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