package com.gmail.wwon.seokk.imagestorage.data.database.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

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
    var image: Bitmap,
    var keyword: String = "",
    var date: Date = Date()
)