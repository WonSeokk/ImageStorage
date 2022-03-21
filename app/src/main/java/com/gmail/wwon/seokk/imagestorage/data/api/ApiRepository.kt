package com.gmail.wwon.seokk.imagestorage.data.api

import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    /**
     * @param request: 이미지/동영상 API 호출을 위한 Model - ReqThumbnail
     * @param isPage: 페이지 스크롤 호출인지 확인
     */
    suspend fun getThumbnails(request: ReqThumbnail, isPage: Boolean): Flow<DataResult<List<Thumbnail>>>
}