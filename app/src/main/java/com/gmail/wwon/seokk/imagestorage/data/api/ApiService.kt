package com.gmail.wwon.seokk.imagestorage.data.api

import com.gmail.wwon.seokk.imagestorage.data.api.models.ImageList
import com.gmail.wwon.seokk.imagestorage.data.api.models.VclipList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    /**
     * @param query: 검색을 원하는 질의어
     * @param sort: 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
     * @param page: 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
     * @param size: 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10
     */
    @GET("/v2/search/image")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getImages(
        @Query("query") query: String,
        @Query("sort") sort: String? = "recency",
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 15
    ): ImageList?

    /**
     * @param query: 검색을 원하는 질의어
     * @param sort: 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
     * @param page: 결과 페이지 번호, 1~15 사이의 값
     * @param size: 한 페이지에 보여질 문서 수, 1~30 사이의 값, 기본 값 15
     */
    @GET("/v2/search/vclip")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getVclips(
        @Query("query") query: String,
        @Query("sort") sort: String? = "recency",
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 15
    ): VclipList?

}