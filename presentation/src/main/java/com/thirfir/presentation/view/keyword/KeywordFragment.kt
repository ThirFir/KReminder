package com.thirfir.presentation.view.keyword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kimcore.inko.Inko
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.thirfir.domain.util.KeywordChecker
import com.thirfir.domain.model.Keyword
import com.thirfir.presentation.R
import com.thirfir.presentation.adapter.KeywordsAdapter
import com.thirfir.presentation.databinding.FragmentKeywordBinding
import com.thirfir.presentation.view.main.MainActivity.Companion.databaseReference
import com.thirfir.presentation.viewmodel.KeywordsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeywordFragment private constructor(): Fragment() {
    private lateinit var binding: FragmentKeywordBinding

    private lateinit var fcmReference: FirebaseMessaging

    private lateinit var map: Map<String, Int> // firebase에 있는 키워드를 가져와서 저장할 변수
    private val viewModel: KeywordsViewModel by viewModels()
    private lateinit var keywordAdapter: KeywordsAdapter
    private var registeredKeywordList = arrayListOf<Keyword>()
    private val inko = Inko()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fcmReference = FirebaseMessaging.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKeywordBinding.inflate(layoutInflater, container, false)

        initEvent()
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerViewKeyword.layoutManager = LinearLayoutManager(requireContext())
        keywordAdapter = KeywordsAdapter {
            unSubscribe(it.name)
        }
        binding.recyclerViewKeyword.adapter = keywordAdapter
    }

    private fun initEvent() {
        databaseReference
            .child("keywords")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    map = dataSnapshot.value as Map<String, Int>
                }
            })

        initClickListeners()
    }

    private fun initClickListeners() {
        with (binding) {
            editTextKeywordInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().trim().isNotEmpty()) {
                        buttonSubscribe.isEnabled = true
                        buttonSubscribe.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary)) // 테스트 색상
                    }
                    else {
                        buttonSubscribe.isEnabled = false
                        buttonSubscribe.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor)) // 테스트 색상
                    }
                }

            })

            buttonSubscribe.setOnClickListener {
                val keyword = editTextKeywordInput.text.toString().trim()
                try {
                    KeywordChecker.check(keyword, registeredKeywordList)
                    subscribe(keyword)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.keywords.observe(viewLifecycleOwner) {
                keywordAdapter.submitList(it)
                registeredKeywordList.clear()
                registeredKeywordList.addAll(it)
            }
        }
    }

    private fun subscribe(keyword: String) {
        showProgress()

        val englishKeyword = inko.ko2en(keyword)

        fcmReference.subscribeToTopic(englishKeyword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val num = map.getOrDefault(keyword, 0) + 1 // 구독자 수 +1

                    databaseReference.child("keywords").child(keyword).setValue(num)
                    viewModel.insertKeyword(keyword)
                } else {
                    Toast.makeText(context, resources.getString(R.string.database_error), Toast.LENGTH_SHORT).show()
                }
                hideProgress()
            }
        binding.editTextKeywordInput.text = null
    }

    private fun unSubscribe(keyword: String) {
        showProgress()
        val englishKeyword = inko.ko2en(keyword)

        fcmReference.unsubscribeFromTopic(englishKeyword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val num = map.getOrDefault(keyword, 1) - 1 // 구독자 수 -1
                    databaseReference.child("keywords").child(keyword).setValue(num)
                    viewModel.deleteKeyword(keyword)
                } else {
                    Toast.makeText(context, resources.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                }
                hideProgress()
            }
    }

    private fun showProgress() {
        this.activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        this.activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        binding.progressBar.visibility = View.GONE
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