package com.unina.natourkt.core.domain.repository

import android.net.Uri
import com.unina.natourkt.core.util.DataState
import java.io.InputStream
import java.net.URL

interface StorageRepository {

    suspend fun uploadFromUri(key: String, uri: Uri): String?

    suspend fun getUrlFromKey(key: String): URL

}