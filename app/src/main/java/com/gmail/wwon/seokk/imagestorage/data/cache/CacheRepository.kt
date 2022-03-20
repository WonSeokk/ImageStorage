package com.gmail.wwon.seokk.imagestorage.data.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.*
import java.net.URL

class CacheRepository constructor(
    private val diskCache: DiskCache,
    private val memoryCache: MemoryCache): Cache {

    override fun put(url: String, bitmap: Bitmap) {
        memoryCache.put(url,bitmap)
        diskCache.put(url,bitmap)
    }

    override fun get(url: String): Bitmap? {
        return memoryCache.get(url)?:diskCache.get(url)
    }

    override fun remove(url: String) {
        memoryCache.remove(url)
        diskCache.remove(url)
    }

    override fun clear() {
        memoryCache.clear()
        diskCache.clear()
    }
}