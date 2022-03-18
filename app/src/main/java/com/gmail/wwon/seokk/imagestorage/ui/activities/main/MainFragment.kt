package com.gmail.wwon.seokk.imagestorage.ui.activities.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.wwon.seokk.imagestorage.databinding.FragmentMainBinding
import com.gmail.wwon.seokk.imagestorage.ui.viewmodels.MainViewModel
import com.gmail.wwon.seokk.imagestorage.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

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

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var viewDataBinding: FragmentMainBinding
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
            thumbnailRecycler.apply {
                layoutManager = GridLayoutManager(context, 3)
                addItemDecoration(GridSpacingItemDecoration(3, 3))
            }
        }
    }
}