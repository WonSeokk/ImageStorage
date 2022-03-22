package com.gmail.wwon.seokk.imagestorage.data.database

import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.Meta
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.database.dao.ThumbnailDao
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Header
import com.gmail.wwon.seokk.imagestorage.data.database.entities.HeaderAndThumbnails
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Storage
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

    override suspend fun getThumbnails(keyword: String): HeaderAndThumbnails = withContext(ioDispatcher) {
        thumbnailDao.getHeaderAndThumbnails(keyword)?.let {
            if(checkHeaderTime(it.header)) {
                it.thumbnails.map { m -> m.isStored = getStorageByURL(m.url) != null }
                return@withContext it
            }
        }
        return@withContext HeaderAndThumbnails.EMPTY
    }

    override suspend fun saveHeader(request: ReqThumbnail, meta: Meta, part: String) = withContext(ioDispatcher) {
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
            return@withContext thumbnailDao.updateHeader(it)
        }
        val newHeader: Header = when(part) {
            IMAGE ->  Header(request.query, request.page, 0, meta.isEnd, false, Date())
            VCLIP -> Header(request.query, 0, request.page, false, meta.isEnd, Date())
            else -> Header.EMPTY
        }
        thumbnailDao.insertHeader(newHeader)
    }

    override suspend fun saveThumbnails(thumbnails: List<Thumbnail>) = withContext(ioDispatcher) { thumbnailDao.insertThumbnails(thumbnails) }

    override suspend fun clearHeader() = withContext(ioDispatcher) { thumbnailDao.clearHeader() }

    suspend fun checkHeaders() {
        thumbnailDao.getHeaders().forEach {
            checkHeaderTime(it)
        }
    }

    //마지막 검색일자 5분 이상 인지 확인
    private suspend fun checkHeaderTime(header: Header): Boolean {
        if(compareDate(header.lastDate) > 5) {
            thumbnailDao.deleteHeaderByKey(header.keyword)
            return false
        }
        return true
    }

    override suspend fun getStorages(): List<Thumbnail> = withContext(ioDispatcher) {
        val thumbnails = mutableListOf<Thumbnail>()
        try{
            thumbnailDao.getStorages()?.forEach { thumbnails.add(Thumbnail(it.url,"", it.date,true)) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        thumbnails.sortBy { it.date }
        return@withContext thumbnails
    }

    override suspend fun getStorageByURL(url: String): Storage? = withContext(ioDispatcher) { thumbnailDao.getStorageByURL(url) }

    override suspend fun deleteStorageByURL(url: String) = withContext(ioDispatcher) { thumbnailDao.deleteStorageByURL(url) }

    override suspend fun insertStorage(url: String) = withContext(ioDispatcher) { thumbnailDao.insertStorage(Storage(url, Date())) }

}