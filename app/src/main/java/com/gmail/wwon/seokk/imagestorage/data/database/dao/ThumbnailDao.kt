package com.gmail.wwon.seokk.imagestorage.data.database.dao

import androidx.room.*
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail

@Dao
interface ThumbnailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThumbnails(thumbnails: List<Thumbnail>)

    @Query("DELETE FROM thumbnails")
    suspend fun clearThumbnail()

    @Query("SELECT * FROM thumbnails")
    suspend fun getThumbnails(): List<Thumbnail>
}