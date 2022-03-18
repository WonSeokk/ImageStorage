package com.gmail.wwon.seokk.imagestorage.data.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * @param collection: 컬렉션
 * @param thumbnailUrl: 미리보기 이미지 URL
 * @param imageUrl: 이미지 URL
 * @param width: 이미지의 가로 길이
 * @param height: 이미지의 세로 길이
 * @param displaySitename: 출처
 * @param docUrl: 문서 URL
 * @param datetime: 문서 작성시간, ISO 8601 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
 */
data class Image (
    @SerializedName("collection") val collection: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("display_sitename") val displaySitename: String,
    @SerializedName("doc_url") val docUrl: String,
    @SerializedName("datetime") val datetime: Date
): Serializable