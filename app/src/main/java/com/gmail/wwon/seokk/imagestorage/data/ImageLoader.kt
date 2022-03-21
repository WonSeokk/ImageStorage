package com.gmail.wwon.seokk.imagestorage.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.widget.AppCompatImageView
import com.gmail.wwon.seokk.imagestorage.R
import com.gmail.wwon.seokk.imagestorage.data.cache.CacheRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.URL

class ImageLoader constructor(
    private val cache: CacheRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main) {

    /**
     * @param url: 로드 URL
     * @param imageview: Bitmap 넣을 이미지 뷰
     */
    fun loadImage(url: String, imageview: AppCompatImageView) {
        CoroutineScope(mainDispatcher).launch {
            //메모리/디스크 캐시 확인
            val bitmap = cache.get(url)
            bitmap?.let {
                imageview.setImageBitmap(it)
                return@launch
            } ?: run {
                //Default image 넣고 URL -> Bitmap 이미지 가져옴
                imageview.tag = url
                imageview.setImageResource(R.drawable.ic_search_image)
                this@ImageLoader.download(url).collect { bitmap ->
                    CoroutineScope(mainDispatcher).launch {
                        when (bitmap) {
                            is DataResult.Success -> {
                                //Bitmap 변환 성공 시 tag 비교 후, setImage & 캐시 추가
                                if (imageview.tag == url)
                                    imageview.setImageBitmap(bitmap.data)
                                cache.put(url, bitmap.data)
                            }
                            //실패 Default 이미지
                            else -> imageview.setImageResource(R.drawable.ic_no_image)
                        }
                    }
                }
            }
        }
    }

    /**
     * @param url: URL -> Bitmap 변환
     */
    private suspend fun download(url: String): Flow<DataResult<Bitmap>> = flow {
        val bitmap: Bitmap?
        try {
            bitmap = URL(url).openStream().use{ BitmapFactory.decodeStream(it) }
            emit(DataResult.Success(bitmap))
        } catch (e: Exception) {
            emit(DataResult.Error(e))
        }
    }.flowOn(ioDispatcher)
}