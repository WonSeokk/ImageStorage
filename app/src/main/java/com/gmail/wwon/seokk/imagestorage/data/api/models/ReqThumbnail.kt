package com.gmail.wwon.seokk.imagestorage.data.api.models

/**
 * @param query: 검색을 원하는 질의어
 * @param sort: 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
 * @param page
 * image - 결과 페이지 번호, 1~50 사이의 값
 * vclip - 결과 페이지 번호, 1~15 사이의 값
 * @param size
 * image - 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10
 * vlip - 한 페이지에 보여질 문서 수, 1~30 사이의 값, 기본 값 15
 */
data class ReqThumbnail (
    var query: String,
    val sort: String? = "recency",
    var page: Int = 1,
    val size: Int? = 15
) {
    companion object {
        val EMPTY = ReqThumbnail("","recency", 1, 15)
    }
}