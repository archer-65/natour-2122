package com.unina.natourkt.domain.repository

import android.net.Uri
import java.net.URL

interface StorageRepository {

    suspend fun uploadFromUri(key: String, uri: Uri): String?

    suspend fun getUrlFromKey(key: String): URL

}