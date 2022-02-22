package com.unina.natourkt.di

import com.unina.natourkt.data.remote.retrofit.CompilationRetrofitDataSource
import com.unina.natourkt.data.remote.retrofit.PostRetrofitDataSource
import com.unina.natourkt.data.remote.retrofit.RouteRetrofitDataSource
import com.unina.natourkt.data.remote.retrofit.UserRetrofitDataSource
import com.unina.natourkt.data.repository.*
import com.unina.natourkt.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Core module to obtain an effective Dependency Inversion.
 * Repository classes are interfaces injected into UseCase classes, in this way
 * we can provide methods to the Domain layer but also avoid implementation's details.
 * This module makes testing easier.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()

    @Provides
    @Singleton
    fun provideUserRepository(retrofitDataSource: UserRetrofitDataSource): UserRepository {
        return UserRepositoryImpl(retrofitDataSource)
    }

    @Provides
    @Singleton
    fun providePostRepository(retrofitDataSource: PostRetrofitDataSource): PostRepository {
        return PostRepositoryImpl(retrofitDataSource)
    }

    @Provides
    @Singleton
    fun provideRouteRepository(retrofitDataSource: RouteRetrofitDataSource): RouteRepository {
        return RouteRepositoryImpl(retrofitDataSource)
    }

    @Provides
    @Singleton
    fun provideCompilationRepository(retrofitDataSource: CompilationRetrofitDataSource): CompilationRepository {
        return CompilationRepositoryImpl(retrofitDataSource)
    }
}