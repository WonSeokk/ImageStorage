package com.gmail.wwon.seokk.imagestorage.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "header")
data class Header (
    @PrimaryKey @ColumnInfo(name = "keyword") var keyword: String = "",
    @ColumnInfo(name = "i_page") var iPage: Int = 1,
    @ColumnInfo(name = "v_page") var vPage: Int = 1,
    @ColumnInfo(name = "i_isEnd") var iIsEnd: Boolean = false,
    @ColumnInfo(name = "v_isEnd") var vIsEnd: Boolean = false,
    @ColumnInfo(name = "last_date") var lastDate: Date = Date(),
)