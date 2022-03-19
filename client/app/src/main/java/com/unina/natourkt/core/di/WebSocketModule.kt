package com.unina.natourkt.core.di

import com.unina.natourkt.core.data.remote.websocket.WsListener
import com.unina.natourkt.core.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener
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

    @Provides
    @Singleton
    fun provideNewWebSocket(): WebSocketListener {
        return WsListener()
    }

    @Provides
    @Singleton
    fun provideRequest(): Request {
        return Request.Builder().url(BASE_URL).build()
    }
}