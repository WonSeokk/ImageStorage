package com.gmail.wwon.seokk.imagestorage

import android.app.Application
import com.gmail.wwon.seokk.imagestorage.data.ImageLoader
import com.gmail.wwon.seokk.imagestorage.data.cache.CacheRepository
import com.gmail.wwon.seokk.imagestorage.data.cache.DiskCache
import com.gmail.wwon.seokk.imagestorage.data.cache.MemoryCache
import com.gmail.wwon.seokk.imagestorage.di.CacheModule
import com.gmail.wwon.seokk.imagestorage.utils.NetworkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ImageStorageApp: Application() {
    lateinit var networkManager: NetworkManager
    lateinit var memoryCache: MemoryCache
    lateinit var diskCache: DiskCache
    lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()
        networkManager = NetworkManager(this)
        memoryCache = MemoryCache(CacheModule.CACHE_SIZE)
        diskCache = DiskCache(this)
        imageLoader = ImageLoader(CacheRepository(diskCache, memoryCache))
    }
}