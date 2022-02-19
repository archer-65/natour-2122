package com.unina.natourkt.di

import com.unina.natourkt.common.Constants.BASE_URL
import com.unina.natourkt.data.remote.retrofit.PostRetrofitDataSource
import com.unina.natourkt.data.remote.retrofit.RouteRetrofitDataSource
import com.unina.natourkt.data.remote.retrofit.UserRetrofitDataSource
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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserRetrofit(retrofit: Retrofit): UserRetrofitDataSource {
        return retrofit.create(UserRetrofitDataSource::class.java)
    }

    @Provides
    @Singleton
    fun providePostRetrofit(retrofit: Retrofit): PostRetrofitDataSource {
        return retrofit.create(PostRetrofitDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideRouteRetrofit(retrofit: Retrofit): RouteRetrofitDataSource {
        return retrofit.create(RouteRetrofitDataSource::class.java)
    }
}