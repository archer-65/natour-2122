package com.unina.natourkt.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun provideOkHttp3WebSocketClient(okHttpClient: OkHttpClient): OkHttpWebSocketClient {
        return OkHttpWebSocketClient(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(okHttpWebSocketClient: OkHttpWebSocketClient): StompClient {
        return StompClient(okHttpWebSocketClient)
    }
}