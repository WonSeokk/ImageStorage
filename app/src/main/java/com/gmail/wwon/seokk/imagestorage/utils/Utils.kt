package com.gmail.wwon.seokk.imagestorage.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.gmail.wwon.seokk.imagestorage.ImageStorageApp
import java.net.URL

/**
 * 네트워크 매니저 (네크워크 상태 체크)
 */
fun Context.networkManager() = (applicationContext as ImageStorageApp).networkManager

fun urlToBitmap(url: String): Bitmap? {
    return try {
        BitmapFactory.decodeStream(URL(url).openStream())
    }
    catch (e: Exception) {
        null
    }
}