package com.unina.natourkt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ticofab.androidgpxparser.parser.GPXParser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Utils {

    @Provides
    @Singleton
    fun provideGpxParser(): GPXParser = GPXParser()
}