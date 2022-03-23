package com.gmail.wwon.seokk.imagestorage.data.database

import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.ImageList
import com.gmail.wwon.seokk.imagestorage.data.api.models.Meta
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.api.models.VclipList
import com.gmail.wwon.seokk.imagestorage.data.database.entities.HeaderAndThumbnails
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Storage
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail

interface LocalRepository {

    /**
     * Header 정보 와 Thumbnail List 가져오기
     * @param keyword: 검색 키워드
     */
    suspend fun getThumbnails(keyword: String): HeaderAndThumbnails?

    /**
     * Header 정보 저장
     * @param request: 요청 Model
     * @param meta: 결과 Meta 정보
     * @param part: image or vclip 구분
     */
    suspend fun saveHeader(request: ReqThumbnail, meta: Meta, part: String)

    /**
     * Thumbnail List 저장
     * @param thumbnails: 저장 할 Thumbnail List
     */
    suspend fun saveThumbnails(thumbnails: List<Thumbnail>)

    /**
     * Header 검사 (최종 검색 후 5분이 지났는지)
     */
    suspend fun checkHeaders()

    /**
     * 내 보관함
     */
    suspend fun getStorages(): List<Thumbnail>?

    /**
     * 내 보관함 찾기
     * @param: url: URL
     */
    suspend fun getStorageByURL(url: String): Storage?

    /**
     * 내 보관함 삭제
     * @param: url: URL
     */
    suspend fun deleteStorageByURL(url: String)

    /**
     * 내 보관함 넣기
     * @param: url: URL
     */
    suspend fun insertStorage(url: String)
}