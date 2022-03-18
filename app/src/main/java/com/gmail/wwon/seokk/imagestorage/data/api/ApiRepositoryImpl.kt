package com.gmail.wwon.seokk.imagestorage.data.api

import android.app.Application
import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class ApiRepositoryImpl constructor(
    private val application: Application,
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ApiRepository {
    override suspend fun getThumbnails(request: ReqThumbnail): Flow<DataResult<List<Thumbnail>>> = flow {
        with(request) {
            val imageList = apiService.getImages(query, sort, page, size)
        }
        emit(DataResult.Error(Exception()))
    }.flowOn(ioDispatcher)
}