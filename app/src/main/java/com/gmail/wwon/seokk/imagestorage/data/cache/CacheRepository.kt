package com.gmail.wwon.seokk.imagestorage.data.cache

import android.graphics.Bitmap

class CacheRepository constructor(
    private val diskCache: DiskCache,
    private val memoryCache: MemoryCache): Cache {

    override fun put(url: String, bitmap: Bitmap) {
        memoryCache.put(url,bitmap)
        diskCache.put(url,bitmap)
    }

    //메모리캐시 -> 디스크캐시 차례대로 확인
    //메모리에 없고 디스크에 있으면 메모리 저장 후 리턴
    override fun get(url: String): Bitmap? {
        return memoryCache.get(url)?:diskCache.get(url)?.let {
            memoryCache.put(url, it)
            it
        }
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