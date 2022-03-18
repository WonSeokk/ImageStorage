package com.gmail.wwon.seokk.imagestorage

import android.app.Application
import com.gmail.wwon.seokk.imagestorage.utils.NetworkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ImageStorageApp: Application() {
    lateinit var networkManager: NetworkManager

    override fun onCreate() {
        super.onCreate()
        networkManager = NetworkManager(this)
    }
}