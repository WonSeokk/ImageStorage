package com.gmail.wwon.seokk.imagestorage.utils

import android.graphics.Bitmap
import android.util.LruCache


object ImageLoader {
    private val lruCache: LruCache<String, Bitmap>

    init {
        val memorySize = Runtime.getRuntime().maxMemory().toInt() / 1024
        val cacheSize = memorySize / 8
        lruCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return value?.byteCount?.div(1024)!!
            }
        }
    }
    /**
     * Add bitmapd to lrucache
     * @param key
     * @param bitmap
     */
    fun addBitmapToLruCache(key: String?, bitmap: Bitmap?) {
        if (getBitmapFromLruCache(key) == null) {
            lruCache.put(key, bitmap)
        }
    }

    /**
     * Get cached bitmap
     * @param key
     */
    fun getBitmapFromLruCache(key: String?): Bitmap? {
        return if (key != null) {
            lruCache.get(key)
        } else null
    }

    /**
     * Move out of cache
     * @param key
     */
    fun removeBitmapFromLruCache(key: String?) {
        if (key != null) {
            lruCache.remove(key)
        }
    }

}