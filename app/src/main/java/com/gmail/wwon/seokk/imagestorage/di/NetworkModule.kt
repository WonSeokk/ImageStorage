package com.gmail.wwon.seokk.imagestorage.di

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.gmail.wwon.seokk.imagestorage.data.api.ApiRepository
import com.gmail.wwon.seokk.imagestorage.data.api.ApiRepositoryImpl
import com.gmail.wwon.seokk.imagestorage.data.api.ApiService
import com.gmail.wwon.seokk.imagestorage.data.database.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@RequiresApi(Build.VERSION_CODES.M)
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://dapi.kakao.com/"
    private const val API_KEY = "KakaoAK eb2c0115f96f8c83b74ca425b7bdb5be"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Api

    @Singleton
    @Provides
    fun provideApiRepository(
        @Api apiService: ApiService,
        localRepository: LocalRepository,
        ioDispatcher: CoroutineDispatcher
    ): ApiRepository {
        return ApiRepositoryImpl(
            apiService, localRepository, ioDispatcher
        )
    }

    @Singleton
    @Api
    @Provides
    fun provideApiService() : ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
            .create(ApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient  = OkHttpClient.Builder().addInterceptor(HeaderInterceptor()).build()

    internal class HeaderInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response =
            chain.proceed(chain.request().newBuilder().addHeader("Authorization", API_KEY).build())
    }
}