package com.unina.natourkt.di

import android.content.Context
import com.unina.natourkt.data.repository.PreferencesRepositoryImpl
import com.unina.natourkt.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module provides injection for [ApplicationContext].
 * Injecting Context avoids possible leaks.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): PreferencesRepository = PreferencesRepositoryImpl(app)
}