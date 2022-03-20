package com.gmail.wwon.seokk.imagestorage.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.widget.AppCompatImageView
import com.gmail.wwon.seokk.imagestorage.R
import com.gmail.wwon.seokk.imagestorage.data.cache.CacheRepository
import kotlinx.coroutines.*
import java.net.URL

class ImageLoader constructor(
    private val cache: CacheRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main) {

    fun loadImage(url: String, imageview: AppCompatImageView) {
        val bitmap = cache.get(url)
        bitmap?.let {
            imageview.setImageBitmap(it)
            return
        }?: run {
            imageview.tag = url
            imageview.setImageResource(R.drawable.ic_bookmark)
            CoroutineScope(ioDispatcher).launch {
                this@ImageLoader.download(url)?.let { bitmap ->
                    cache.put(url, bitmap)
                    CoroutineScope(mainDispatcher).launch { loadImage(url, imageview) }
                }
            }
        }
    }

    private suspend fun download(url: String): Bitmap? = withContext(ioDispatcher) {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeStream(URL(url).openStream())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext bitmap
    }
}