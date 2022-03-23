package com.gmail.wwon.seokk.imagestorage.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * 내 보관함 정보
 * @param url: 저장 한 URl
 * @param date: 저장 날짜
 */
@Entity(tableName = "storage")
data class Storage (
    @PrimaryKey val url: String,
    var date: Date = Date()
)