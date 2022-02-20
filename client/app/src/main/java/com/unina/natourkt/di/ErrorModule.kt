package com.unina.natourkt.di

import com.unina.natourkt.common.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Useful module to inject [ErrorHandler].
 * Used by any kind of class which wants to rely on Exceptions' abstracted handling.
 * At the moment is Exceptions are managed by UseCase classes and LoadState (Paging 3 library)
 */
@Module
@InstallIn(SingletonComponent::class)
object ErrorModule {

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler = ErrorHandler()
}