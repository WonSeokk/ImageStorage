package com.gmail.wwon.seokk.imagestorage.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gmail.wwon.seokk.imagestorage.ImageStorageApp
import com.gmail.wwon.seokk.imagestorage.R
import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.ApiRepository
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import com.gmail.wwon.seokk.imagestorage.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val apiRepository: ApiRepository
) : BaseViewModel(application) {

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


    fun searchThumbnail(text: String, isPage: Boolean) = viewModelScope.launch {
        if(isPaging.value!!) return@launch
        if(text.isEmpty()) {
            context.toast(context.getString(R.string.msg_search_fill))
            return@launch
        }

        request.query = text
        apiRepository.getThumbnails(request, isPage).collect { result ->
            when(result) {
                is DataResult.Loading -> { _isPaging.value = result.isLoading }
                is DataResult.Success -> { _thumbnailList.value = result.data }
                is DataResult.Error -> { result.ex.message.also { _errMsg.value = it } }
            }
        }
    }
}