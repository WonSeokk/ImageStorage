package com.gmail.wwon.seokk.imagestorage.ui.activities.main

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gmail.wwon.seokk.imagestorage.R
import com.gmail.wwon.seokk.imagestorage.data.api.ApiRepositoryImpl
import com.gmail.wwon.seokk.imagestorage.databinding.ActivityMainBinding
import com.gmail.wwon.seokk.imagestorage.ui.viewmodels.MainViewModel
import com.gmail.wwon.seokk.imagestorage.utils.setBadge
import com.gmail.wwon.seokk.imagestorage.utils.toast
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity: AppCompatActivity() {

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
            TabLayoutMediator(tab, viewPager) { tab, position -> tab.text = pageList[position].title
                tab.orCreateBadge.apply {
                    this.badgeTextColor = ContextCompat.getColor(this@MainActivity, android.R.color.white)
                    this.isVisible = false
                }}.attach()
        }
        mainViewModel.apply {
            storageList.observe(this@MainActivity) {
                binding.tab.getTabAt(pageList.indexOf(Tab(resources.getString(R.string.title_storage), MainFragment.STORAGE_PAGE)))?.badge?.setBadge(it.size)
            }
            errMsg.observe(this@MainActivity) {
                if(it == ApiRepositoryImpl.NO_NETWORK) toast(this@MainActivity.getString(R.string.msg_no_network))
            }
        }
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