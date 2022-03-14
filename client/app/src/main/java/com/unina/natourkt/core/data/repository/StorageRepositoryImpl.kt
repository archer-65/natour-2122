package com.unina.natourkt.core.data.repository

import android.util.Log
import com.amplifyframework.kotlin.core.Amplify
import com.unina.natourkt.core.domain.repository.StorageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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