package com.gmail.wwon.seokk.imagestorage.data.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * @param title: 동영상 제목
 * @param playTime: 동영상 재생시간, 초 단위
 * @param thumbnail: 동영상 미리보기 URL
 * @param url: 동영상 링크
 * @param datetime:	동영상 등록일, ISO 8601 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
 * @param author: 동영상 업로더
 */
data class Vclip (
    @SerializedName("title") val title: String,
    @SerializedName("play_time") val playTime: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("url") val url: String,
    @SerializedName("datetime") val datetime: Date,
    @SerializedName("author") val author: String
): Serializable