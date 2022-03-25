package com.unina.natourkt.core.domain.repository

import android.net.Uri
import java.net.URL

/**
 * Interface for storage related functions repository
 */
interface StorageRepository {

    /**
     * This function uploads a file (given its [Uri]) with a key
     */
    suspend fun uploadFromUri(key: String, uri: Uri): String?

    /**
     * This function gets the url of a file given its key
     */
    suspend fun getUrlFromKey(key: String): URL
}