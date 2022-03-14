package com.unina.natourkt.core.domain.repository

import java.io.InputStream
import java.net.URL

interface StorageRepository {

    suspend fun uploadFromUri(key: String, stream: InputStream): String?

    suspend fun getUrlFromKey(key: String): URL

}