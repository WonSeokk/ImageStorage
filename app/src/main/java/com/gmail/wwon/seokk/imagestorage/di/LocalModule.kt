package com.gmail.wwon.seokk.imagestorage.di

import android.content.Context
import androidx.room.Room
import com.gmail.wwon.seokk.imagestorage.data.database.LocalDatabase
import com.gmail.wwon.seokk.imagestorage.data.database.dao.ThumbnailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDatabase::class.java,
            "Local.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideThumbnailDao(localDatabase: LocalDatabase): ThumbnailDao = localDatabase.thumbnailDao()
}