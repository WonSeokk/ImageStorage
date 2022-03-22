package com.gmail.wwon.seokk.imagestorage.ui.activities.main

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gmail.wwon.seokk.imagestorage.R
import com.gmail.wwon.seokk.imagestorage.databinding.ActivityMainBinding
import com.gmail.wwon.seokk.imagestorage.ui.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var pageList: List<Tab>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pageList = listOf(Tab(resources.getString(R.string.title_search), MainFragment.SEARCH_PAGE), Tab(resources.getString(R.string.title_storage), MainFragment.STORAGE_PAGE))

        binding.apply {
            lifecycleOwner = this@MainActivity
            mainViewModel = this@MainActivity.mainViewModel

            viewPager.apply {
                this@MainActivity.pagerAdapter = ViewPagerAdapter(this@MainActivity)
                adapter = this@MainActivity.pagerAdapter
            }
            TabLayoutMediator(tab, viewPager) { tab, position -> tab.text = pageList[position].title }.attach()
        }
    }

    override fun onStart() {
        super.onStart()
        scope.launch { mainViewModel.getStorage() }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }


    private inner class ViewPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = pageList.size
        override fun createFragment(position: Int): Fragment {
            return MainFragment.newInstance(pageList[position].page)
        }
    }

    /**
     * @param title: Tab 이름
     * @param page: Fragment Page KEY
     */
    private data class Tab(
        val title: String,
        val page: String
    )
}