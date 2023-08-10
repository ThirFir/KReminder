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

        // 추가 데이터 아이템들...
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))

        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))
        postItems.add(PostItem(30976, "일반공지", BASE_URL.addQueryString("b", 14, 30976)))
        postItems.add(PostItem(30978, "장학공지", BASE_URL.addQueryString("b", 14, 30978)))


        // 추가 데이터 아이템들...

        // 초기에 첫 페이지의 데이터를 표시합니다.
        updateRecyclerViewData()

        return binding.root
    }


    private fun initClickListeners() {
        postAdapter = PostAdapter { postItem ->
            val contentFragment = ContentFragment.newInstance(postItem.url)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, contentFragment)
                .addToBackStack(null)
                .commit()
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
        val sublist = postItems.subList(start, end)
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

        // 다음 페이지의 버튼들을 업데이트합니다.
        binding.page1Button.text = (currentPage + 1).toString()
        binding.page2Button.text = (currentPage + 2).toString()
        binding.page3Button.text = (currentPage + 3).toString()
        binding.page4Button.text = (currentPage + 4).toString()
        binding.page5Button.text = (currentPage + 5).toString()
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
