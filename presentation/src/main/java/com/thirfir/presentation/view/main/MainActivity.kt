package com.thirfir.presentation.view.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.thirfir.presentation.R
import com.thirfir.presentation.databinding.ActivityMainBinding
import com.thirfir.presentation.view.keyword.KeywordFragment
import com.thirfir.presentation.view.post.ContentFragment
import com.thirfir.presentation.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel : PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        authReference = FirebaseAuth.getInstance()

        if ( userIdCheck() ) {
            initViews()
            viewModel
            setOnPageItemClicked()
        }

    }

    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .add(binding.mainFragmentContainer.id, BulletinBoardFragment.newInstance())
            .commit()
    }

    private fun userIdCheck(): Boolean {
        var ret = true
        if (authReference.currentUser == null) {
            authReference.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    ret = if (task.isSuccessful) {
                        true
                    } else {
                        Toast.makeText(this, "네트워크를 연결한 뒤 실행시켜주세요.", Toast.LENGTH_SHORT).show()
                        false
                    }
                }
        }

        return ret
    }

    private fun setOnPageItemClicked() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_bulletin_board_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.mainFragmentContainer.id, BulletinBoardFragment.newInstance())
                        .commit()
                    true
                }
                R.id.item_bookmark_page -> {
                    // TODO : 즐겨찾기 페이지
                    true
                }
                R.id.item_keyword_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(binding.mainFragmentContainer.id, KeywordFragment.newInstance())
                        .commit()
                    true
                }
                R.id.item_setting_page -> {
                    true
                }

                else -> false
            }
        }
    }

    companion object {
        lateinit var databaseReference: DatabaseReference
        lateinit var authReference: FirebaseAuth
    }
}
