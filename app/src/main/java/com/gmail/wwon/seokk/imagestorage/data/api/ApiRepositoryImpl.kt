package com.gmail.wwon.seokk.imagestorage.data.api

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.cache.CacheRepository
import com.gmail.wwon.seokk.imagestorage.data.database.LocalRepository
import com.gmail.wwon.seokk.imagestorage.data.database.LocalRepositoryImpl
import com.gmail.wwon.seokk.imagestorage.data.database.entities.HeaderAndThumbnails
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.URL

@RequiresApi(Build.VERSION_CODES.M)
class ApiRepositoryImpl constructor(
    private val application: Application,
    private val apiService: ApiService,
    private val localRepository: LocalRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ApiRepository {

    override suspend fun getThumbnails(request: ReqThumbnail, isPage: Boolean): Flow<DataResult<List<Thumbnail>>> = flow {
        try {
            emit(DataResult.Loading(true))
            val thumbnails = mutableListOf<Thumbnail>()
            var data: HeaderAndThumbnails = HeaderAndThumbnails.EMPTY

            localRepository.getThumbnails(request.query).let { dataResult ->
                when(dataResult) {
                    is DataResult.Success -> { data = dataResult.data }
                    is DataResult.Error -> throw Exception(dataResult.ex.message)
                    else -> {}
                }
            }

            if((data.header.iPage == 0 && data.header.vPage == 0) || isPage) {
                if(!data.header.iIsEnd) {
                    request.page = data.header.iPage + 1
                    apiService.getImages(request.query, request.sort, request.page, request.size).let { imageList ->
                        if(imageList.meta.totalCount > 0) {
                            localRepository.saveHeader(request, imageList.meta, LocalRepositoryImpl.IMAGE)
                            imageList.images.forEach { image ->
                                thumbnails.add(Thumbnail(image.thumbnailUrl, request.query, image.datetime))
                            }
                        }
                    }
                }
                if(!data.header.vIsEnd) {
                    request.page = data.header.vPage + 1
                    apiService.getVclips(request.query, request.sort, request.page, request.size).let { vclipList ->
                        if(vclipList.meta.totalCount > 0) {
                            localRepository.saveHeader(request, vclipList.meta, LocalRepositoryImpl.VCLIP)
                            vclipList.vclips.forEach { vclip ->
                                thumbnails.add(Thumbnail(vclip.thumbnail, request.query, vclip.datetime))
                            }
                        }
                    }
                }
                if(thumbnails.size > 0)
                    localRepository.saveThumbnails(thumbnails)
            }

            thumbnails.addAll(data.thumbnails)
            thumbnails.sortByDescending { s -> s.date }
            emit(DataResult.Success(thumbnails))

        } catch (e: Exception) {
            emit(DataResult.Error(e))
        } finally {
            emit(DataResult.Loading(false))
        }
    }.flowOn(ioDispatcher)

}