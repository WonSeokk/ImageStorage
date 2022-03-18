package com.gmail.wwon.seokk.imagestorage.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gmail.wwon.seokk.imagestorage.ImageStorageApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    protected val context
        get() = getApplication<Application>()
}