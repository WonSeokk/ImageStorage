package com.gmail.wwon.seokk.imagestorage.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gmail.wwon.seokk.imagestorage.data.database.dao.ThumbnailDao
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Header
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail

@Database(entities = [Header::class, Thumbnail::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun thumbnailDao(): ThumbnailDao
}