package com.gmail.wwon.seokk.imagestorage.utils

import android.graphics.Bitmap
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gmail.wwon.seokk.imagestorage.R
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("android:toVisible")
fun View.setVisible(value: Boolean) {
    isVisible = value
    isGone = !value
}

/**
 * TextInputLayout - EndIcon Click Listener
 */
@BindingAdapter("android:endIconOnClickListener")
fun TextInputLayout.setEndIconClick(onClickListener: View.OnClickListener) {
    setEndIconOnClickListener(onClickListener)
}

@BindingAdapter("android:setSpinnerColor")
fun SwipeRefreshLayout.setSpinner(color: Int) = setColorSchemeColors(color, color, color)
