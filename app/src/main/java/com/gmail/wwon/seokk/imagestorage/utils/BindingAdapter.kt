package com.gmail.wwon.seokk.imagestorage.utils

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gmail.wwon.seokk.imagestorage.R
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * visibility
 */
@BindingAdapter("android:toVisible")
fun View.setVisible(value: Boolean) {
    isVisible = value
    isGone = !value
}

/**
 * 날짜 Format
 */
@SuppressLint("SimpleDateFormat")
@BindingAdapter("android:dateFormat")
fun AppCompatTextView.dateFormat(date: Date) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss", Locale.getDefault())
        formatter.format(dateTime)
    } else {
        val formatter = SimpleDateFormat("yy.MM.dd HH:mm:ss", Locale.getDefault())
        formatter.format(date)
    }
}

/**
 * TextInputLayout - EndIcon Click Listener
 */
@BindingAdapter("android:endIconOnClickListener")
fun TextInputLayout.setEndIconClick(onClickListener: View.OnClickListener) {
    setEndIconOnClickListener(onClickListener)
}

/**
 * SwipeLayout Spinner 색상
 */
@BindingAdapter("android:setSpinnerColor")
fun SwipeRefreshLayout.setSpinner(color: Int) = setColorSchemeColors(color, color, color)

/**
 * ImageView bookMark 표시
 */
@BindingAdapter("android:setSrc")
fun AppCompatImageView.setSrc(value: Boolean) {
    if(value) setColorFilter(ContextCompat.getColor(context, R.color.primaryColor), android.graphics.PorterDuff.Mode.SRC_IN)
    else setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray), android.graphics.PorterDuff.Mode.SRC_IN)
}
