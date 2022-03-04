package com.unina.natourkt.data.repository

import android.net.Uri
import android.util.Log
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.kotlin.storage.Storage
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageListOptions
import com.amplifyframework.storage.result.StorageUploadInputStreamResult
import com.unina.natourkt.domain.repository.StorageRepository
import com.unina.natourkt.presentation.main.MainActivity
import kotlinx.coroutines.*
import retrofit2.http.Url
import java.io.IOException
import java.io.InputStream
import java.net.URL

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class StorageRepositoryImpl : StorageRepository {

    override suspend fun uploadFromUri(key: String, stream: InputStream): String {
        val uploadResult = Amplify.Storage.uploadInputStream(key, stream)
        Log.i("S3 Upload", "${uploadResult.result()}")
        return uploadResult.result().key
    }

    override suspend fun getUrlFromKey(key: String): URL {
        val result = Amplify.Storage.getUrl(key)
        Log.i("S3 Retrieve", "$result")
        return result.url
    }
}