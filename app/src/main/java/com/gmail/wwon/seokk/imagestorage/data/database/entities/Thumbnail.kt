package com.gmail.wwon.seokk.imagestorage.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

/**
 * Thumbnail 정보
 * @param keyword: 검색 한 Keyword
 * @param date: Thumbnail 날짜
 * @param isStored: 내 보관함에 있는지 ?
 */
@Entity(tableName = "thumbnails",
    foreignKeys = [ForeignKey(
        entity = Header::class,
        parentColumns = arrayOf("keyword"),
        childColumns = arrayOf("keyword"),
        onDelete = CASCADE
    )]
)
data class Thumbnail (
    @PrimaryKey val url: String,
    var keyword: String = "",
    var date: Date = Date(),
    var isStored: Boolean = false
)