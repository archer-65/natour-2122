package com.unina.natourkt.core.di

import android.content.res.Resources
import com.unina.natourkt.BuildConfig
import com.unina.natourkt.core.data.remote.retrofit.*
import com.unina.natourkt.core.util.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * This module is only for [Retrofit], but requires generation of implementation
 * through Builder pattern.
 * @see [Retrofit.Builder]
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideHttpClient(networkConnectionInterceptor: NetworkConnectionInterceptor) =
        OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor).build()

    /**
     * Retrofit object is pretty much the same, so we define URL and Converter here, only one time
     * @see [BASE_URL]
     * @see [GsonConverterFactory]
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserRetrofit(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostRetrofit(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRouteRetrofit(retrofit: Retrofit): RouteApi {
        return retrofit.create(RouteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCompilationRetrofit(retrofit: Retrofit): CompilationApi {
        return retrofit.create(CompilationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReportRetrofit(retrofit: Retrofit): ReportApi {
        return retrofit.create(ReportApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRatingRetrofit(retrofit: Retrofit): RatingApi {
        return retrofit.create(RatingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRetrofit(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMapsRetrofit(retrofit: Retrofit): MapsApi {
        return retrofit.create(MapsApi::class.java)
    }
}