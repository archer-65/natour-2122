package com.unina.natourkt.di

import com.unina.natourkt.data.remote.repository.data.AmplifyAuthDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AmplifyModule {

    @Provides
    @Singleton
    fun provideAmplify(): AmplifyAuthDataSource {
        return AmplifyAuthDataSource()
    }
}