package com.unina.natourkt.data.repository

import android.net.Uri
import android.util.Log
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.kotlin.storage.Storage
import com.amplifyframework.storage.result.StorageUploadInputStreamResult
import com.unina.natourkt.domain.repository.StorageRepository
import com.unina.natourkt.presentation.main.MainActivity
import kotlinx.coroutines.*
import retrofit2.http.Url
import java.io.InputStream
import java.net.URL

class StorageRepositoryImpl : StorageRepository {

    @OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override suspend fun uploadFromUri(uri: Uri): String? {

        var result: Storage.InProgressStorageOperation<StorageUploadInputStreamResult>? = null
        CoroutineScope(Dispatchers.IO).launch {
            val deferred = GlobalScope.async(Dispatchers.Default) {
                val stream = MainActivity.instance.contentResolver.openInputStream(uri)!!
                Amplify.Storage.uploadInputStream("exampleKey", stream)
                //Log.i("Upload S3", "${upload?.result()?.key}")
            }
            result = deferred.await()
        }

        return result?.result()?.key
    }

    override suspend fun getUrlFromKey(key: String): URL {
        val result = Amplify.Storage.getUrl(key)
        Log.i("S3 get url result", "$result")
        return result.url
    }
}