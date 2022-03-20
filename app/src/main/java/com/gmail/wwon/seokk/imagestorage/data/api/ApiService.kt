package com.gmail.wwon.seokk.imagestorage.data.api

import com.gmail.wwon.seokk.imagestorage.data.api.models.ImageList
import com.gmail.wwon.seokk.imagestorage.data.api.models.VclipList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET("/v2/search/image")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getImages(
        @Query("query") query: String,
        @Query("sort") sort: String? = "recency",
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 15
    ): ImageList

    @GET("/v2/search/vclip")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getVclips(
        @Query("query") query: String,
        @Query("sort") sort: String? = "recency",
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 15
    ): VclipList

}