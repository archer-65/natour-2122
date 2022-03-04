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

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class StorageRepositoryImpl : StorageRepository {

    override suspend fun uploadFromUri(key: String, uri: Uri): String? {

        val uploadResult = withContext(Dispatchers.IO) {
            val stream = runCatching {
                MainActivity.instance.contentResolver.openInputStream(uri)
            }
            stream.getOrThrow()
                ?.let { Amplify.Storage.uploadInputStream(key, it) }
        }

        Log.i("S3 Upload Status", "${uploadResult?.result()}")
        return uploadResult?.result()?.key
    }

    override suspend fun getUrlFromKey(key: String): URL {
        val result = Amplify.Storage.getUrl(key)
        Log.i("S3 get url result", "$result")
        return result.url
    }
}