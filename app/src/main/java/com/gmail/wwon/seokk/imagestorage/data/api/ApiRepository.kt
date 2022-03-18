package com.gmail.wwon.seokk.imagestorage.data.api

import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    suspend fun getThumbnails(request: ReqThumbnail): Flow<DataResult<List<Thumbnail>>>
}