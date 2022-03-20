package com.gmail.wwon.seokk.imagestorage.data.cache

import android.graphics.Bitmap

interface Cache {
    fun put(url: String, bitmap: Bitmap)
    fun get(url: String): Bitmap?
    fun remove(url: String)
    fun clear()
}