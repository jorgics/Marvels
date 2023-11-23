package com.compose.marvels.core.di

import com.compose.marvels.BuildConfig
import com.compose.marvels.core.interceptors.AuthInterceptor
import com.compose.marvels.data.network.MarvelsRepository
import com.compose.marvels.data.network.MarvelsService
import com.compose.marvels.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    fun provideMarvelsService(retrofit: Retrofit): MarvelsService {
        return retrofit.create(MarvelsService::class.java)
    }

    @Provides
    fun provideRepository(apiService: MarvelsService): Repository {
        return MarvelsRepository(apiService)
    }
}