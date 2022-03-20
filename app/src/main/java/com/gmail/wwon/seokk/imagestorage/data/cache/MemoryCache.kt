package com.gmail.wwon.seokk.imagestorage.data.cache

import android.graphics.Bitmap
import android.util.LruCache
import javax.inject.Singleton

@Singleton
class MemoryCache (newMaxSize: Int): Cache {
    companion object {
        private val MAX_MEMORY = Runtime.getRuntime().maxMemory() / 1024
        private val DEFAULT_SIZE = (MAX_MEMORY/4).toInt()
    }
    private val cache : LruCache<String, Bitmap>

    init {
        val cacheSize : Int = if (newMaxSize > MAX_MEMORY) DEFAULT_SIZE else newMaxSize
        cache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return (value.rowBytes)*(value.height)/1024
            }
        }
    }

    override fun put(url: String, bitmap: Bitmap) { cache.put(url,bitmap) }

    override fun get(url: String): Bitmap? = cache.get(url)

    override fun remove(url: String) { cache.remove(url) }

    override fun clear() = cache.evictAll()

}