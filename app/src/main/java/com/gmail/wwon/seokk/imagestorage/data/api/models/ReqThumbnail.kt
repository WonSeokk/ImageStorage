package com.gmail.wwon.seokk.imagestorage.data.api.models

/**
 * @param query: 검색을 원하는 질의어
 * @param sort: 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
 * @param page: 결과 페이지 번호, 1~15 사이의 값
 * @param size: 한 페이지에 보여질 문서 수, 1~30 사이의 값, 기본 값 15
 */
data class ReqThumbnail (
    val query: String,
    val sort: String? = "recency",
    val page: Int? = 1,
    val size: Int? = 15
)