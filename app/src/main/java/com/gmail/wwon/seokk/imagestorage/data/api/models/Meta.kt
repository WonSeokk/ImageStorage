package com.gmail.wwon.seokk.imagestorage.data.api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @param totalCount: 검색된 문서 수
 * @param pageableCount: total_count 중 노출 가능 문서 수
 * @param isEnd: 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
 */
data class Meta (
    @SerializedName("total_count") val totalCount: String,
    @SerializedName("pageable_count") val pageableCount: String,
    @SerializedName("is_end") val isEnd: Boolean
): Serializable