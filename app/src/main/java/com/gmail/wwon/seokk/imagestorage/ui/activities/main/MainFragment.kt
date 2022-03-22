package com.gmail.wwon.seokk.imagestorage.ui.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.wwon.seokk.imagestorage.R
import com.gmail.wwon.seokk.imagestorage.databinding.FragmentMainBinding
import com.gmail.wwon.seokk.imagestorage.ui.activities.main.adapter.ThumbnailAdapter
import com.gmail.wwon.seokk.imagestorage.ui.viewmodels.MainViewModel
import com.gmail.wwon.seokk.imagestorage.utils.GridSpacingItemDecoration
import com.gmail.wwon.seokk.imagestorage.utils.toVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
    private var viewDataBinding: FragmentMainBinding? = null
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
        thumbnailAdapter = ThumbnailAdapter(mainViewModel)
        return viewDataBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.apply {
            lifecycleOwner = this@MainFragment.viewLifecycleOwner
            mainViewModel = this@MainFragment.mainViewModel
            inputSearch.visibility = (page == SEARCH_PAGE).toVisibility()

            thumbnailRecycler.apply {
                layoutManager = GridLayoutManager(context, 3)
                addItemDecoration(GridSpacingItemDecoration(3, 5))
                adapter = thumbnailAdapter
            }
        }

        when(page) {
            SEARCH_PAGE -> bindSearch()
            STORAGE_PAGE -> bindStorage()
        }

    }

    //메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        viewDataBinding = null
    }

    //검색 Fragment
    private fun bindSearch() {
        viewDataBinding?.apply {
            //당겨서 새로고침
            layoutSwipe.setOnRefreshListener {
                this@MainFragment.mainViewModel.apply {
                    scope.launch { searchThumbnail(request.query,
                        isPage = false,
                        isSwipe = true,
                        view = null
                    ) }
                }
            }
            //스크롤 이벤트
            scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if(v.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) && scrollY > oldScrollY) {
                        this@MainFragment.mainViewModel.apply {
                            scope.launch { searchThumbnail(request.query,
                                isPage = true,
                                isSwipe = false,
                                view = null
                            ) }
                        }
                    }
                }
            })
            //스크롤 Top 확인
            scrollView.viewTreeObserver.addOnScrollChangedListener {
                layoutSwipe.isEnabled = (scrollView.scrollY == 0)
            }
        }
        mainViewModel.apply {
            isProgress.observe(this@MainFragment.viewLifecycleOwner) {
                if(!it) viewDataBinding!!.scrollView.fullScroll(NestedScrollView.FOCUS_UP)
            }
            thumbnailList.observe( this@MainFragment.viewLifecycleOwner) {
                viewDataBinding!!.emptyList.apply {
                    if(it.isEmpty()) {
                        text = context.getString(R.string.empty_search)
                        visibility = View.VISIBLE
                    } else visibility = View.GONE
                }
                thumbnailAdapter.update(it)
            }
        }
    }

    //보관함 Fragment
    private fun bindStorage() {
        viewDataBinding?.apply {
            layoutSwipe.isEnabled = false
        }

        mainViewModel.apply {
            storageList.observe( this@MainFragment.viewLifecycleOwner) {
                viewDataBinding!!.emptyList.apply {
                    if(it.isEmpty()) {
                        text = context.getString(R.string.empty_storage)
                        visibility = View.VISIBLE
                    } else visibility = View.GONE
                }
                thumbnailAdapter.update(it)
            }
        }
    }

}
