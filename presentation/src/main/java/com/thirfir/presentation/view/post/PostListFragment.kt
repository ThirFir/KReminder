package com.thirfir.presentation.view.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thirfir.domain.BULLETIN_QUERY
import com.thirfir.domain.PID
import com.thirfir.presentation.adapter.BulletinBoardsAdapter
import com.thirfir.presentation.adapter.PostAdapter
import com.thirfir.presentation.databinding.FragmentPostListBinding
import com.thirfir.presentation.model.BulletinBoardItem
import com.thirfir.presentation.viewmodel.PostHeadersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PostListFragment : Fragment() {

    private lateinit var binding: FragmentPostListBinding
    private lateinit var postAdapter: PostAdapter
    private var currentPage = 1
    private var pageNum=0;

    private lateinit var onBulletinBoardClickListener: (BulletinBoardItem) -> Unit

    private val ioDispatcher = Dispatchers.IO
    private val ioScope = CoroutineScope(ioDispatcher)

    private val bulletin: Int by lazy {
        requireActivity().intent.getIntExtra("b", 0)
    }
    private val postHeadersViewModel: PostHeadersViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postHeadersViewModel.fetchPostHeaders(bulletin, currentPage)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostListBinding.inflate(inflater, container, false)

        initClickListeners()
        initRecyclerView()
        initPaginationButtons()
        slideMenu()

        return binding.root
    }


    private fun slideMenu(){
        // 초기화
        binding.recyclerViewMenu.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val MenuItems = listOf(
            BulletinBoardItem("일반공지", 14),
            BulletinBoardItem("장학공지", 15),
            BulletinBoardItem("학사공지", 16),
            BulletinBoardItem("학생생활", 21),
            BulletinBoardItem("채용공지", 150),
            BulletinBoardItem("현장실습공지", 151),
            BulletinBoardItem("사회봉사공지", 191),
            BulletinBoardItem("자유게시판", 22),
        )

        onBulletinBoardClickListener = {
            val intent = Intent(requireContext(), PostListActivity::class.java).apply {
                putExtra(BULLETIN_QUERY, it.bulletin)
            }
            startActivity(intent)
        }
        binding.recyclerViewMenu.adapter=BulletinBoardsAdapter(MenuItems,onBulletinBoardClickListener)

        //아이콘 클릭시 메뉴 열기
        binding.postMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.recyclerViewMenu)
        }

    }

    private fun initClickListeners() {
        binding.searchButton.setOnClickListener {
            val searchText = binding.searchEditText.text.toString()

            // TODO : 검색 기능 구현
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireContext())
        postAdapter = PostAdapter(
            postHeadersViewModel.postHeaders.value
        ) { postListItem ->
            val intent = Intent(requireContext(), PostActivity::class.java).apply {
                putExtra(PID, postListItem.pid)
                putExtra(BULLETIN_QUERY, this@PostListFragment.bulletin)
                putExtra("header", postListItem)
            }

            startActivity(intent)
        }
        binding.recyclerViewPost.adapter = postAdapter

        binding.recyclerViewPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // 스크롤 상태가 변할 때 호출됨
                if (!recyclerView.canScrollVertically(1)) {
                    binding.pageButtonsLayout.visibility = View.VISIBLE
                } else {
                    binding.pageButtonsLayout.visibility = View.GONE
                }
            }
        })
        ioScope.launch {
            postHeadersViewModel.postHeaders.collect(
                collector = { postHeaders ->
                    postAdapter.updateData(postHeaders)
                }
            )
        }
    }

    private fun initPaginationButtons() {

        binding.prevButton.setOnClickListener {
            // TODO :
            if (currentPage > 5) {
                currentPage-=5
                binding.page1Button.text=(binding.page1Button.text.toString().toInt()-5).toString()
                binding.page2Button.text=(binding.page2Button.text.toString().toInt()-5).toString()
                binding.page3Button.text=(binding.page3Button.text.toString().toInt()-5).toString()
                binding.page4Button.text=(binding.page4Button.text.toString().toInt()-5).toString()
                binding.page5Button.text=(binding.page5Button.text.toString().toInt()-5).toString()

                postHeadersViewModel.fetchPostHeaders(bulletin, currentPage)
            }
        }

        binding.nextButton.setOnClickListener {
            currentPage+=5// 5페이지씩 이동
            binding.page1Button.text=(5 + binding.page1Button.text.toString().toInt()).toString()
            binding.page2Button.text=(5 + binding.page2Button.text.toString().toInt()).toString()
            binding.page3Button.text=(5 + binding.page3Button.text.toString().toInt()).toString()
            binding.page4Button.text=(5 + binding.page4Button.text.toString().toInt()).toString()
            binding.page5Button.text=(5 + binding.page1Button.text.toString().toInt()).toString()
            postHeadersViewModel.fetchPostHeaders(bulletin, currentPage)
        }

        // 각 페이지 버튼에 대한 클릭 리스너 설정
        binding.page1Button.setOnClickListener {
            if(currentPage == pageNum*5+1)
                return@setOnClickListener
            currentPage = pageNum*5+1
            postHeadersViewModel.fetchPostHeaders(bulletin, currentPage)
        }

        binding.page2Button.setOnClickListener {
            if(currentPage == pageNum*5+2)
                return@setOnClickListener
            currentPage = pageNum*5+2
            postHeadersViewModel.fetchPostHeaders(bulletin, currentPage)
        }

        binding.page3Button.setOnClickListener {
            if(currentPage == pageNum*5+3)
                return@setOnClickListener
            currentPage = pageNum*5+3
            postHeadersViewModel.fetchPostHeaders(bulletin, currentPage)
        }

        binding.page4Button.setOnClickListener {
            if(currentPage == pageNum*5+4)
                return@setOnClickListener
            currentPage = pageNum*5+4
            postHeadersViewModel.fetchPostHeaders(bulletin, currentPage)
        }

        binding.page5Button.setOnClickListener {
            if(currentPage == pageNum*5+5)
                return@setOnClickListener
            currentPage = pageNum*5+5
            postHeadersViewModel.fetchPostHeaders(bulletin, currentPage)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PostListFragment().apply {
                arguments = Bundle().apply {
                    // Add any arguments if needed
                }
            }
    }
}