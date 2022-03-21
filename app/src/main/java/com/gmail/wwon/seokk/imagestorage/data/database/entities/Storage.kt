package com.gmail.wwon.seokk.imagestorage.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "storage")
data class Storage (
    @PrimaryKey val url: String,
    var date: Date = Date()
)