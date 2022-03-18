package com.gmail.wwon.seokk.imagestorage.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position (아이템 포지션)
        if (position >= 0) {
            val column = position % spanCount // item column (아이템 열)
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing) (왼쪽 적용)
            outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f / spanCount) * spacing) (오른쪽 적용)
            if (position >= spanCount) {
                outRect.top = spacing // item top (아이텀 행 두번째 부터 Top 적용)
            }
        } else {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
            outRect.bottom = 0
        }
    }
}