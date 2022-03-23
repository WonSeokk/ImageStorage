package com.gmail.wwon.seokk.imagestorage.data.database.dao

import androidx.room.*
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Header
import com.gmail.wwon.seokk.imagestorage.data.database.entities.HeaderAndThumbnails
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Storage
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail

@Dao
interface ThumbnailDao {

    @Query("SELECT * FROM header")
    suspend fun getHeaders(): List<Header>

    @Query("DELETE FROM header WHERE keyword = :keyword")
    suspend fun deleteHeaderByKey(keyword: String)

    @Query("SELECT * FROM header WHERE keyword = :keyword")
    suspend fun getHeaderByKey(keyword: String): Header?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeader(header: Header)

    @Update
    suspend fun updateHeader(header: Header)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThumbnails(thumbnails: List<Thumbnail>)

    @Transaction
    @Query("SELECT * FROM header WHERE keyword = :keyword")
    suspend fun getHeaderAndThumbnails(keyword: String): HeaderAndThumbnails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStorage(storage: Storage)

    @Query("DELETE FROM storage WHERE url = :url")
    suspend fun deleteStorageByURL(url: String)

    @Query("SELECT * FROM storage")
    suspend fun getStorages(): List<Storage>?

    @Query("SELECT * FROM storage WHERE url = :url")
    suspend fun getStorageByURL(url: String): Storage?
}