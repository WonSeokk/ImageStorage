package com.gmail.wwon.seokk.imagestorage.data.cache

import android.graphics.Bitmap

interface Cache {
    /**
     * 캐시 저장
     * @param url: Key 값이 됨
     * @param bitmap: 변환 된 Bitmap
     */
    fun put(url: String, bitmap: Bitmap)

    /**
     * 캐시 가져옮
     * @param url: Key 값이 됨
     */
    fun get(url: String): Bitmap?

    //캐시 정보 지우기 (호출 안함)
    fun remove(url: String)
    fun clear()
}