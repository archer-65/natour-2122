package com.unina.natourkt.di

import com.unina.natourkt.common.Constants.BASE_URL
import com.unina.natourkt.data.remote.PostRetrofitDataSource
import com.unina.natourkt.data.remote.UserRetrofitDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideUserRetrofit(): UserRetrofitDataSource {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserRetrofitDataSource::class.java)
    }

    @Provides
    @Singleton
    fun providePostRetrofit(): PostRetrofitDataSource {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostRetrofitDataSource::class.java)
    }
}