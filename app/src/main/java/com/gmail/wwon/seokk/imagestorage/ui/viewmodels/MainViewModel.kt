package com.gmail.wwon.seokk.imagestorage.ui.viewmodels

import android.app.Application
import android.os.Build
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
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
import com.gmail.wwon.seokk.imagestorage.utils.networkManager
import com.gmail.wwon.seokk.imagestorage.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.M)
class MainViewModel @Inject constructor(
    application: Application,
    private val apiRepository: ApiRepository,
    private val localRepository: LocalRepository
) : BaseViewModel(application) {

    init {
        //데이터 기록 정보 확인
        viewModelScope.launch { localRepository.checkHeaders() }
        viewModelScope.launch { getStorage() }
    }

    var markClickListener: ((Thumbnail) -> Unit)? = null

    val searchText = MutableLiveData<String>().apply { value = "" }

    val request = ReqThumbnail.EMPTY

    private val _isPaging = MutableLiveData<Boolean>().apply { value = false }
    val isPaging: LiveData<Boolean>
        get() = _isPaging

    private val _isSwiping = MutableLiveData<Boolean>().apply { value = false }
    val isSwiping: LiveData<Boolean>
        get() = _isSwiping

    private val _isProgress = MutableLiveData<Boolean>().apply { value = false }
    val isProgress: LiveData<Boolean>
        get() = _isProgress

    private val _errMsg = MutableLiveData<String>()
    val errMsg: LiveData<String>
        get() = _errMsg

    private val _thumbnailList = MutableLiveData<List<Thumbnail>>().apply { value = listOf() }
    val thumbnailList: LiveData<List<Thumbnail>>
        get() = _thumbnailList

    private val _storageList = MutableLiveData<MutableList<Thumbnail>>().apply { value =  mutableListOf() }
    val storageList: LiveData<MutableList<Thumbnail>>
        get() = _storageList


    //Thumbnail 검색
    fun searchThumbnail(text: String, isPage: Boolean, isSwipe: Boolean, view: View?) = viewModelScope.launch {
        //로딩 중이면 return
        if(isPaging.value!! || isSwiping.value!! || isProgress.value!!) return@launch
        //검색어 빈값 return
        if(text.isEmpty()) {
            context.toast(context.getString(R.string.msg_search_fill))
            _isSwiping.value = false
            return@launch
        }
        //키보드 내리기
        view?.let { context.hideKeyboard(it) }

        request.query = text
        apiRepository.getThumbnails(request, isPage).collect { result ->
            viewModelScope.launch {
                when(result) {
                    is DataResult.Loading -> {
                        result.isLoading.also {
                            when {
                                isPage -> _isPaging.value = it
                                isSwipe -> _isSwiping.value = it
                                !isPage && !isSwipe -> _isProgress.value = it
                            }
                        }
                    }
                    is DataResult.Success -> { _thumbnailList.value = result.data }
                    is DataResult.Error -> { result.ex.message.also { _errMsg.value = it } }
                }
            }
        }
    }

    //내 보관함
    private fun getStorage() = viewModelScope.launch {
        localRepository.getStorages()?.let { _storageList.value = it.toMutableList() }
    }

    //Bookmark Click
    fun clickBookmark(thumbnail: Thumbnail) = viewModelScope.launch {
        //로딩 중이면 return
        if(isPaging.value!! || isSwiping.value!! || isProgress.value!!) return@launch
        if(thumbnail.isStored)
            localRepository.deleteStorageByURL(thumbnail.url)
        else
            localRepository.insertStorage(thumbnail.url)
        getStorage()
        markClickListener?.invoke(thumbnail)
    }


    //검색 키보드 엔터 키 리스너
    fun keySearch(): View.OnKeyListener {
        return View.OnKeyListener { view, key, _ ->
            if(key == KeyEvent.KEYCODE_ENTER) {
                searchThumbnail(searchText.value!!, isPage = false, isSwipe = false, view = view)
                return@OnKeyListener true
            }
            return@OnKeyListener false
        }
    }
}