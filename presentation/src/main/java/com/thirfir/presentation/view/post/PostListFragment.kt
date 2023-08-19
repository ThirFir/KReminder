package com.thirfir.presentation.view.post

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.presentation.R
import com.thirfir.presentation.adapter.PostAdapter
import com.thirfir.presentation.databinding.FragmentPostListBinding
import com.thirfir.presentation.model.PostItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListFragment : Fragment() {

    private lateinit var binding: FragmentPostListBinding
    private lateinit var postAdapter: PostAdapter
    private var currentPage = 1
    private val postsPerPage = 20


    val BASE_URL = "https://portal.koreatech.ac.kr/ctt/bb/bulletin"

    fun String.addQueryString(query: String, number: Int, postnum: Int): String {
        return if (this.contains("?"))
            "$this&$query=$number&ls=20&ln=1&dm=r&p=$postnum"
        else "$this?$query=$number&ls=20&ln=1&dm=r&p=$postnum"
    }


    private val postListItems = mutableListOf<PostItem>() // 빈 목록 생성
  //  private val viewModel: PostListViewModel by viewModels() //? = null// activityViewModels 사용

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostListBinding.inflate(inflater, container, false)

        initClickListeners()
        initRecyclerView()
        initPaginationButtons()
        slideMenu()
        //   viewModel = ViewModelProvider(this).get(PostListViewModel::class.java)
        // 추가 데이터 아이템들...
        postListItems.add(PostItem(30978, "hi", BASE_URL.addQueryString("b", 14, 30978)))
        postListItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))

        // 초기에 첫 페이지의 데이터를 표시
        updateRecyclerViewData()
        //    updateViewModelValue()

        return binding.root
    }

//    private fun updateViewModelValue() {
//        viewModel.deleteAll() // ViewModel에서 값을 변경하는 메서드 호출
//    }


    private fun slideMenu(){
        val drawerLayout = binding.drawerLayout
        val navigationView = binding.navigationView

// NavigationView의 메뉴 아이템 클릭 리스너 설정
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    //아이템 각 게시판에 맞게 생성해놓고 버튼 클릭할 때마다 리스트에 적용하면 될듯
                    true
                }
                R.id.menu_item2 -> {
                    true
                }
                R.id.menu_item3 -> {
                    true
                }
                R.id.menu_item4 -> {
                    true
                }
                R.id.menu_item5 -> {
                    true
                }
                R.id.menu_item6 -> {
                    true
                }
                R.id.menu_item7 -> {
                    true
                }
                R.id.menu_item8 -> {
                    true
                }
                // 다른 메뉴 아이템들에 대한 처리도 추가 가능
                else -> false
            }
        }

// post_menu 버튼 클릭 시 NavigationView를 열기/닫기
        binding.postMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView)
            } else {
                drawerLayout.openDrawer(navigationView)
            }
        }


    }


    // ... (이전 코드 생략)

    private fun initClickListeners() {
        postAdapter = PostAdapter { postItem ->
            val intent = Intent(requireContext(), PostListActivity::class.java)
            intent.putExtra("url", postItem.url)
            startActivity(intent)
        }
        binding.searchButton.setOnClickListener {
            val searchText = binding.searchEditText.text.toString()
            val filteredItems = postListItems.filter { it.title.contains(searchText, ignoreCase = true) }
            updateRecyclerViewData(filteredItems)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireContext())
        postAdapter = PostAdapter { postListItem ->
            val intent = Intent(requireContext(), PostListActivity::class.java)
            intent.putExtra("url", postListItem.url)
            startActivity(intent)
        }
        binding.recyclerViewPost.adapter = postAdapter



    }

    private fun initPaginationButtons() {
        // 버튼 초기 상태 업데이트
        updateButtonState()

        binding.prevButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                updateRecyclerViewData()
            }
        }

        binding.nextButton.setOnClickListener {
            val totalPage = (postListItems.size + postsPerPage - 1) / postsPerPage
            if (currentPage < totalPage) {
                currentPage++
                updateRecyclerViewData()
            }
        }

        // 각 페이지 버튼에 대한 클릭 리스너 설정
        binding.page1Button.setOnClickListener {
            currentPage = 1
            updateRecyclerViewData()
        }

        binding.page2Button.setOnClickListener {
            currentPage = 2
            updateRecyclerViewData()
        }

        binding.page3Button.setOnClickListener {
            currentPage = 3
            updateRecyclerViewData()
        }

        binding.page4Button.setOnClickListener {
            currentPage = 4
            updateRecyclerViewData()
        }

        binding.page5Button.setOnClickListener {
            currentPage = 5
            updateRecyclerViewData()
        }
    }


    private fun updateRecyclerViewData() {
        val start = (currentPage - 1) * postsPerPage
        val end = minOf(start + postsPerPage, postListItems.size)

        if (start < end) { // Check if start is less than end before creating the sublist
            val sublist = postListItems.subList(start, end)
            postAdapter.submitList(sublist)
        } else {
            postAdapter.submitList(emptyList()) // Submit an empty list if start >= end
        }


        updateButtonState()
    }
    private fun updateRecyclerViewData(newList: List<PostItem>) {//특정 단어를 포함하는 아이템 목록으로 업데이트
        val start = (currentPage - 1) * postsPerPage
        val end = minOf(start + postsPerPage, newList.size)
        val sublist = newList.subList(start, end)
        postAdapter.submitList(sublist)
        /*viewModel.allPostList.observe(viewLifecycleOwner) { postList ->
             postListAdapter.updateData(postListItems)
         }*/
        updateButtonState()
    }

    private fun updateButtonState() {
        val totalPage = (postListItems.size + postsPerPage - 1) / postsPerPage
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
            PostListFragment().apply {
                arguments = Bundle().apply {
                    // Add any arguments if needed
                }
            }
    }
}