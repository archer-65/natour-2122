package com.unina.natourkt.domain.repository

import android.net.Uri
import java.io.InputStream
import java.net.URL

interface StorageRepository {

    suspend fun uploadFromUri(key: String, stream: InputStream): String?

    suspend fun getUrlFromKey(key: String): URL

}