package com.unina.natourkt.core.di

import com.unina.natourkt.core.data.remote.dto.mapper.*
import com.unina.natourkt.core.data.remote.retrofit.*
import com.unina.natourkt.core.data.repository.*
import com.unina.natourkt.core.domain.repository.*
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
    fun provideAuthRepository(
        preferencesRepository: PreferencesRepository,
        userApiMapper: UserApiMapper,
        userApi: UserApi
    ): AuthRepository = AuthRepositoryImpl(preferencesRepository, userApiMapper, userApi)

    @Provides
    @Singleton
    fun provideStorageRepository(): StorageRepository = StorageRepositoryImpl()

    @Provides
    @Singleton
    fun provideMapsRepository(
        api: MapsApi,
        directionsApiMapper: DirectionsApiMapper
    ): MapsRepository {
        return MapsRepositoryImpl(api, directionsApiMapper)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserApi): UserRepository {
        return UserRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun providePostRepository(
        api: PostApi,
        postApiMapper: PostApiMapper,
        postCreationApiMapper: PostCreationApiMapper
    ): PostRepository {
        return PostRepositoryImpl(api, postApiMapper, postCreationApiMapper)
    }

    @Provides
    @Singleton
    fun provideRouteRepository(
        api: RouteApi,
        routeApiMapper: RouteApiMapper,
        routeTitleApiMapper: RouteTitleApiMapper,
        routeCreationApiMapper: RouteCreationApiMapper
    ): RouteRepository {
        return RouteRepositoryImpl(api, routeApiMapper, routeTitleApiMapper, routeCreationApiMapper)
    }

    @Provides
    @Singleton
    fun provideCompilationRepository(
        api: CompilationApi,
        compilationApiMapper: CompilationApiMapper
    ): CompilationRepository {
        return CompilationRepositoryImpl(api, compilationApiMapper)
    }
}