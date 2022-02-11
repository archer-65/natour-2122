package com.unina.natourkt.di

import com.unina.natourkt.common.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorModule {

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler = ErrorHandler()
}