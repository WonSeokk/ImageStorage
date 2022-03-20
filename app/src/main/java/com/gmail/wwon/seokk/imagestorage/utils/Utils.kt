package com.gmail.wwon.seokk.imagestorage.utils

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.gmail.wwon.seokk.imagestorage.ImageStorageApp
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*


/**
 * Toast Message
 */
fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/**
 * 네트워크 매니저 (네크워크 상태 체크)
 */
fun Context.networkManager() = (applicationContext as ImageStorageApp).networkManager

/**
 * 두 날짜 시간(분) 차이 구하기
 */
fun compareDate(lastDate: Date): Long {
    val lastCal = Calendar.getInstance()
    val currentCal = Calendar.getInstance()
    lastCal.time = lastDate
    currentCal.time = Date()
    return (currentCal.timeInMillis - lastCal.timeInMillis).toMin()
}

/**
 * timeStamp -> 분으로 계산하기
 */
fun Long.toMin(): Long = ((this / (1000 * 60 )) % 60 )

/**
 * url -> 비트맵 변환
 */
//fun String.toBitmap(): Bitmap = BitmapFactory.decodeStream(URL(this).openStream())
fun String.toBitmap(context: Application): Bitmap? {
    var test = this
    if(this.isBlank()) test = "https://search2.kakaocdn.net/argon/138x78_80_pr/ARspTVK7ka5"
    var bitmap: Bitmap? = null
    Glide.with(context)
        .asBitmap()
        .load(test)
        .into(object : CustomTarget<Bitmap?>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap?>?
            ) {
                bitmap = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    return bitmap
}

/**
 * View Visibility
 * true -> VISIBLE
 * false -> GONE
 */
fun Boolean.toVisibility(): Int = if (this) View.VISIBLE else View.GONE

