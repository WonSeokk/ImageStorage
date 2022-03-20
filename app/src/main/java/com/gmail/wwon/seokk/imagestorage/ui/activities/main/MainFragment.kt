package com.gmail.wwon.seokk.imagestorage.ui.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.wwon.seokk.imagestorage.databinding.FragmentMainBinding
import com.gmail.wwon.seokk.imagestorage.ui.activities.main.adapter.ThumbnailAdapter
import com.gmail.wwon.seokk.imagestorage.ui.viewmodels.MainViewModel
import com.gmail.wwon.seokk.imagestorage.utils.GridSpacingItemDecoration
import com.gmail.wwon.seokk.imagestorage.utils.toVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.cast

@AndroidEntryPoint
class MainFragment: Fragment() {
    companion object {
        private const val PAGE = "PAGE"
        const val SEARCH_PAGE = "ORDER_PAGE"
        const val STORAGE_PAGE = "STORAGE_PAGE"

        fun newInstance(page: String?) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(PAGE, page)
                }
            }
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var viewDataBinding: FragmentMainBinding
    private lateinit var thumbnailAdapter: ThumbnailAdapter
    private lateinit var page: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getString(PAGE).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentMainBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.apply {
            lifecycleOwner = this@MainFragment.viewLifecycleOwner
            mainViewModel = this@MainFragment.mainViewModel
            thumbnailAdapter = ThumbnailAdapter(this@MainFragment.mainViewModel)
            inputSearch.visibility = (page == SEARCH_PAGE).toVisibility()

            thumbnailRecycler.apply {
                itemAnimator = null
                layoutManager = GridLayoutManager(context, 3)
                addItemDecoration(GridSpacingItemDecoration(3, 5))
                adapter = thumbnailAdapter
            }

            layoutSwipe.setOnRefreshListener {
                this@MainFragment.mainViewModel.apply {
                    scope.launch { searchThumbnail(searchText.value!!, false) }
                }
            }

            //스크롤 이벤트
            scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if(v.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) && scrollY > oldScrollY) {
                        this@MainFragment.mainViewModel.apply {
                            scope.launch { searchThumbnail(request.query,true) }
                        }
                    }
                }
            })

            scrollView.viewTreeObserver.addOnScrollChangedListener {
                layoutSwipe.isEnabled = (scrollView.scrollY == 0)
            }

        }

        mainViewModel.apply {
            thumbnailList.observe( this@MainFragment.viewLifecycleOwner) {
                if(page == SEARCH_PAGE)
                    thumbnailAdapter.update(it)
            }
        }
    }
}