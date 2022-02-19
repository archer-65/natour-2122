package com.unina.natourkt.di

import com.unina.natourkt.data.remote.retrofit.PostRetrofitDataSource
import com.unina.natourkt.data.remote.retrofit.RouteRetrofitDataSource
import com.unina.natourkt.data.remote.retrofit.UserRetrofitDataSource
import com.unina.natourkt.data.repository.AuthRepositoryImpl
import com.unina.natourkt.data.repository.PostRepositoryImpl
import com.unina.natourkt.data.repository.RouteRepositoryImpl
import com.unina.natourkt.data.repository.UserRepositoryImpl
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.PostRepository
import com.unina.natourkt.domain.repository.RouteRepository
import com.unina.natourkt.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
}