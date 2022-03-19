package com.unina.natourkt.core.data.remote.websocket

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WsListener : WebSocketListener() {

    val TAG: String = "WEBSOCKET STATE"

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d(TAG, "WebSocket onClosed called")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "WebSocketListener onClosing called")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d(TAG, "throwable - $t() called")
        Log.d(TAG, "response - $response() called")
        Log.d(TAG, "WebSocketListener - onFailure() called")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d(TAG, "WebSocketListener onMessage called")
        Log.d(TAG, "text -> $text() called")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        Log.d(TAG, "WebSocketListener onMessage called")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d(TAG, "WebSocketListener onMessage called")
    }
}