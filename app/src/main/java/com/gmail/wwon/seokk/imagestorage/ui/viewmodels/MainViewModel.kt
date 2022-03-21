package com.gmail.wwon.seokk.imagestorage.ui.viewmodels

import android.app.Application
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gmail.wwon.seokk.imagestorage.R
import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.ApiRepository
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.database.LocalRepository
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import com.gmail.wwon.seokk.imagestorage.utils.hideKeyboard
import com.gmail.wwon.seokk.imagestorage.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val apiRepository: ApiRepository,
    private val localRepository: LocalRepository
) : BaseViewModel(application) {

    init {
        //데이터 기록 정보 초기화
        viewModelScope.launch { localRepository.clearHeader() }
    }

    val searchText = MutableLiveData<String>()

    val request = ReqThumbnail.EMPTY

    private val _isPaging = MutableLiveData<Boolean>().apply { value = false }
    val isPaging: LiveData<Boolean>
        get() = _isPaging

    private val _isProgress = MutableLiveData<Boolean>().apply { value = false }
    val isProgress: LiveData<Boolean>
        get() = _isProgress

    private val _errMsg = MutableLiveData<String>()
    val errMsg: LiveData<String>
        get() = _errMsg

    private val _thumbnailList = MutableLiveData<List<Thumbnail>>().apply { listOf<Thumbnail>() }
    val thumbnailList: LiveData<List<Thumbnail>>
        get() = _thumbnailList


    fun searchThumbnail(text: String, isPage: Boolean, view: View?) = viewModelScope.launch {
        //이미 로딩 중이면 return
        if(isPaging.value!!) return@launch
        //검색어 빈값 return
        if(text.isEmpty()) {
            context.toast(context.getString(R.string.msg_search_fill))
            return@launch
        }
        //키보드 내리기
        view?.let { context.hideKeyboard(it) }

        request.query = text
        apiRepository.getThumbnails(request, isPage).collect { result ->
            viewModelScope.launch {
                when(result) {
                    is DataResult.Loading -> { _isPaging.value = result.isLoading }
                    is DataResult.Success -> { _thumbnailList.value = result.data }
                    is DataResult.Error -> { result.ex.message.also { _errMsg.value = it } }
                }
            }
        }
    }

    //검색 키보드 엔터 키 리스너
    fun keySearch(): View.OnKeyListener {
        return View.OnKeyListener { view, key, _ ->
            if(key == KeyEvent.KEYCODE_ENTER) {
                searchThumbnail(searchText.value!!, false, view)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        }
    }
}