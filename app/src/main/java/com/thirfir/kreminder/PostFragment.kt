import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.kreminder.ContentFragment
import com.thirfir.kreminder.R
import com.thirfir.kreminder.databinding.FragmentPostBinding
import com.thirfir.kreminder.item.PostItem

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postAdapter: PostAdapter
    private var currentPage = 1
    private val postsPerPage = 20


    val BASE_URL = "https://portal.koreatech.ac.kr/ctt/bb/bulletin"

    fun String.addQueryString(query: String, number: Int, postnum: Int): String {
        return if (this.contains("?"))
            "$this&$query=$number&ls=20&ln=1&dm=r&p=$postnum"
        else "$this?$query=$number&ls=20&ln=1&dm=r&p=$postnum"
    }

    private val postItems = mutableListOf<PostItem>() // 빈 목록 생성

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        initClickListeners()
        initRecyclerView()
        initPaginationButtons()
        slideMenu()

        // 추가 데이터 아이템들...
        postItems.add(PostItem(1, "hi", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "hi", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(2, "hi", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "hi", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(3, "hi", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "hi", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))




        // 초기에 첫 페이지의 데이터를 표시합니다.
        updateRecyclerViewData()

        return binding.root
    }

    private fun slideMenu(){
        val drawerLayout = binding.drawerLayout
        val navigationView = binding.navigationView

// NavigationView의 메뉴 아이템 클릭 리스너 설정
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item1 -> {
                    // 메뉴 아이템 1 클릭 시 처리

                    true
                }
                R.id.menu_item2 -> {
                    // 메뉴 아이템 2 클릭 시 처리

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


    private fun initClickListeners() {
        postAdapter = PostAdapter { postItem ->
            val contentFragment = ContentFragment.newInstance(postItem.url)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, contentFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.searchButton.setOnClickListener {
            val searchText = binding.searchEditText.text.toString()
            val filteredItems = postItems.filter { it.title.contains(searchText, ignoreCase = true) }
            updateRecyclerViewData(filteredItems)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewPost.layoutManager = LinearLayoutManager(requireContext())
        postAdapter = PostAdapter { postItem ->
            val contentFragment = ContentFragment.newInstance(postItem.url)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, contentFragment)
                .addToBackStack(null)
                .commit()
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
            val totalPage = (postItems.size + postsPerPage - 1) / postsPerPage
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
        val end = minOf(start + postsPerPage, postItems.size)

        if (start < end) { // Check if start is less than end before creating the sublist
            val sublist = postItems.subList(start, end)
            postAdapter.submitList(sublist)
        } else {
            postAdapter.submitList(emptyList()) // Submit an empty list if start >= end
        }

        updateButtonState()
    }
    private fun updateRecyclerViewData(newList: List<PostItem>) {
        val start = (currentPage - 1) * postsPerPage
        val end = minOf(start + postsPerPage, newList.size)
        val sublist = newList.subList(start, end)
        postAdapter.submitList(sublist)
        updateButtonState()
    }

    private fun updateButtonState() {
        val totalPage = (postItems.size + postsPerPage - 1) / postsPerPage
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
            PostFragment().apply {
                arguments = Bundle().apply {
                    // Add any arguments if needed
                }
            }
    }
}