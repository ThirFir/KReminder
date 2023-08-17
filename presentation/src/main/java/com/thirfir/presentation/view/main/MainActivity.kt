package com.thirfir.presentation.view.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.thirfir.presentation.databinding.ActivityMainBinding
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
        }

    }

    private fun initViews() {
        supportFragmentManager.beginTransaction()
            .add(binding.mainFragmentContainer.id, ContentFragment.newInstance(15, 1826))
            //.add(binding.mainFragmentContainer.id, ContentFragment.newInstance(14, 30975))
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

    companion object {
        lateinit var databaseReference: DatabaseReference
        lateinit var authReference: FirebaseAuth
    }
}
