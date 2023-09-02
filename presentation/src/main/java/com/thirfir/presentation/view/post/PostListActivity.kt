package com.thirfir.presentation.view.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.thirfir.presentation.adapter.BulletinBoardsAdapter
import com.thirfir.presentation.databinding.ActivityPostListBinding
import com.thirfir.presentation.model.BulletinBoardItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostListActivity : AppCompatActivity(){
    private val binding: ActivityPostListBinding by lazy {
        ActivityPostListBinding.inflate(layoutInflater)
    }
    private var bulletin: Int = 0
    private var bulletinTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bulletin = intent.getIntExtra("b", 0)
        bulletinTitle = intent.getStringExtra("title") ?: ""

        initDrawerLayout()
        initView()
        initClickListeners()
    }

    private fun initView() {
        binding.topAppBar.title = bulletinTitle
        supportFragmentManager.beginTransaction()
            .add(binding.postFragmentContainer.id, PostListFragment.newInstance(bulletin, bulletinTitle))
            .commit()
    }

    private fun initDrawerLayout(){
        binding.recyclerViewMenu.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        binding.recyclerViewMenu.adapter = BulletinBoardsAdapter(BulletinBoardItem.values().toList()) {
            bulletin = it.bulletin
            bulletinTitle = it.title

            supportFragmentManager.beginTransaction()
                .replace(binding.postFragmentContainer.id, PostListFragment.newInstance(bulletin, bulletinTitle))
                .commit()
            binding.topAppBar.title = bulletinTitle
        }
    }

    private fun initClickListeners() {
        binding.topAppBar.run {
            setNavigationOnClickListener {
                binding.drawerLayout.openDrawer(binding.recyclerViewMenu)
            }
        }
    }
}