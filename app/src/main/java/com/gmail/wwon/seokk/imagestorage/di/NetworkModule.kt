package com.gmail.wwon.seokk.imagestorage.di

import android.app.Application
import com.gmail.wwon.seokk.imagestorage.data.api.ApiRepository
import com.gmail.wwon.seokk.imagestorage.data.api.ApiRepositoryImpl
import com.gmail.wwon.seokk.imagestorage.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://dapi.kakao.com/"
    private const val API_KEY = "eb2c0115f96f8c83b74ca425b7bdb5be"

    @Singleton
    @Provides
    fun provideApiRepository(
        application: Application,
        @Singleton apiService: ApiService,
        ioDispatcher: CoroutineDispatcher
    ): ApiRepository {
        return ApiRepositoryImpl(
            application, apiService, ioDispatcher
        )
    }

    @Singleton
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