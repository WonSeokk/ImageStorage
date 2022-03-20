package com.gmail.wwon.seokk.imagestorage.data.database

import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.ImageList
import com.gmail.wwon.seokk.imagestorage.data.api.models.Meta
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.api.models.VclipList
import com.gmail.wwon.seokk.imagestorage.data.database.entities.HeaderAndThumbnails
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail

interface LocalRepository {
    suspend fun getThumbnails(keyword: String): DataResult<HeaderAndThumbnails>

    suspend fun saveHeader(request: ReqThumbnail, meta: Meta, part: String)

    suspend fun saveThumbnails(thumbnails: List<Thumbnail>)
}