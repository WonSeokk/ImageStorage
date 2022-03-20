package com.gmail.wwon.seokk.imagestorage.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class HeaderAndThumbnails (
    @Embedded
    var header: Header,
    @Relation(parentColumn = "keyword", entityColumn = "keyword")
    var thumbnails: List<Thumbnail>
) {
    companion object {
        val EMPTY = HeaderAndThumbnails(Header.EMPTY, arrayListOf())
    }
}