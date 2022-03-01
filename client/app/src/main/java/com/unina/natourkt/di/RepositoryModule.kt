package com.unina.natourkt.di

import com.unina.natourkt.data.remote.retrofit.CompilationApi
import com.unina.natourkt.data.remote.retrofit.PostApi
import com.unina.natourkt.data.remote.retrofit.RouteApi
import com.unina.natourkt.data.remote.retrofit.UserApi
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
    fun provideUserRepository(api: UserApi): UserRepository {
        return UserRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePostRepository(api: PostApi): PostRepository {
        return PostRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideRouteRepository(api: RouteApi): RouteRepository {
        return RouteRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCompilationRepository(api: CompilationApi): CompilationRepository {
        return CompilationRepositoryImpl(api)
    }
}