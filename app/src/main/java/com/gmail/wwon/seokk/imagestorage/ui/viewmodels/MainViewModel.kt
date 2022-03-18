package com.gmail.wwon.seokk.imagestorage.ui.viewmodels

import android.app.Application
import com.gmail.wwon.seokk.imagestorage.ImageStorageApp
import com.gmail.wwon.seokk.imagestorage.data.api.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val apiRepository: ApiRepository
) : BaseViewModel(application) {

}