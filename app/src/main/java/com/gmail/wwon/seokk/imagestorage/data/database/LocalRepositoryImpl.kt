package com.gmail.wwon.seokk.imagestorage.data.database

import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.Meta
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.database.dao.ThumbnailDao
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Header
import com.gmail.wwon.seokk.imagestorage.data.database.entities.HeaderAndThumbnails
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import com.gmail.wwon.seokk.imagestorage.utils.compareDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class LocalRepositoryImpl constructor(
    private val thumbnailDao: ThumbnailDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LocalRepository {

    companion object {
        const val IMAGE = "IMAGE"
        const val VCLIP = "VCLIP"
    }

    override suspend fun getThumbnails(keyword: String): DataResult<HeaderAndThumbnails> = withContext(ioDispatcher) {
        thumbnailDao.getHeaderAndThumbnails(keyword)?.let {
            if(checkHeaderTime(it.header))
                return@withContext DataResult.Success(it)
        }
        return@withContext DataResult.Success(HeaderAndThumbnails.EMPTY)
    }

    override suspend fun saveHeader(request: ReqThumbnail, meta: Meta, part: String) {
        thumbnailDao.getHeaderByKey(request.query)?.let {
            when(part) {
                IMAGE -> {
                    it.iPage = request.page
                    it.iIsEnd = meta.isEnd
                }
                VCLIP -> {
                    it.vPage = request.page
                    it.vIsEnd = meta.isEnd
                }
            }
            it.lastDate = Date()
            return thumbnailDao.updateHeader(it)
        }
        val newHeader: Header = when(part) {
            IMAGE ->  Header(request.query, request.page, 0, meta.isEnd, false, Date())
            VCLIP -> Header(request.query, 0, request.page, false, meta.isEnd, Date())
            else -> Header.EMPTY
        }
        thumbnailDao.insertHeader(newHeader)
    }

    override suspend fun saveThumbnails(thumbnails: List<Thumbnail>) = thumbnailDao.insertThumbnails(thumbnails)

    override suspend fun clearHeader() = thumbnailDao.clearHeader()

    suspend fun checkHeaders() {
        thumbnailDao.getHeaders().forEach {
            checkHeaderTime(it)
        }
    }

    private suspend fun checkHeaderTime(header: Header): Boolean {
        if(compareDate(header.lastDate) > 5) {
            thumbnailDao.deleteHeaderByKey(header.keyword)
            return false
        }
        return true
    }

}