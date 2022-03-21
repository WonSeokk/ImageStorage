package com.gmail.wwon.seokk.imagestorage.di

import android.content.Context
import com.gmail.wwon.seokk.imagestorage.data.ImageLoader
import com.gmail.wwon.seokk.imagestorage.data.cache.Cache
import com.gmail.wwon.seokk.imagestorage.data.cache.CacheRepository
import com.gmail.wwon.seokk.imagestorage.data.cache.DiskCache
import com.gmail.wwon.seokk.imagestorage.data.cache.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    const val CACHE_SIZE = 10*1024*1024

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Disk

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Memory

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CacheRepo

    @Singleton
    @Memory
    @Provides
    fun provideMemoryCache() = MemoryCache(CACHE_SIZE)

    @Singleton
    @Disk
    @Provides
    fun provideDiskCache(@ApplicationContext context: Context) = DiskCache(context)

    @Singleton
    @CacheRepo
    @Provides
    fun provideCacheRepository(
        @Disk diskCache: DiskCache,
        @Memory memoryCache: MemoryCache
    ): Cache {
        return CacheRepository(
            diskCache, memoryCache
        )
    }
    
    @Singleton
    @Provides
    fun provideImageLoader(@CacheRepo cacheRepository: CacheRepository) = ImageLoader(cacheRepository)
}