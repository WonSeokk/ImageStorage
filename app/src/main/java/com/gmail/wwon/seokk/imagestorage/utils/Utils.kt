package com.gmail.wwon.seokk.imagestorage.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.gmail.wwon.seokk.imagestorage.ImageStorageApp
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
 * 이미지 로더
 */
fun Context.imageLoader() = (applicationContext as ImageStorageApp).imageLoader

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
 * View Visibility
 * true -> VISIBLE
 * false -> GONE
 */
fun Boolean.toVisibility(): Int = if (this) View.VISIBLE else View.GONE

