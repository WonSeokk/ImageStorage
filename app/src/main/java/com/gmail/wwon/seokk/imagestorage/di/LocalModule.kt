package com.gmail.wwon.seokk.imagestorage.di

import android.content.Context
import androidx.room.Room
import com.gmail.wwon.seokk.imagestorage.data.database.LocalDatabase
import com.gmail.wwon.seokk.imagestorage.data.database.LocalRepository
import com.gmail.wwon.seokk.imagestorage.data.database.LocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
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
    fun provideLocalRepository(
        database: LocalDatabase,
        ioDispatcher: CoroutineDispatcher
    ): LocalRepository {
        return LocalRepositoryImpl(
            database.thumbnailDao(), ioDispatcher
        )
    }

}