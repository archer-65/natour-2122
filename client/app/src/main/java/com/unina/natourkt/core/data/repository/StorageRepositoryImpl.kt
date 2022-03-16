package com.unina.natourkt.core.data.repository

import android.net.Uri
import android.util.Log
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StorageException
import com.unina.natourkt.core.domain.repository.StorageRepository
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.toInputStream
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class StorageRepositoryImpl : StorageRepository {

    override suspend fun uploadFromUri(key: String, uri: Uri): String? {
        return try {
            val stream = uri.toInputStream().getOrThrow()
            val uploadResult = stream?.let { Amplify.Storage.uploadInputStream(key, it) }

            Log.i("S3 Upload", "${uploadResult!!.result()}")
            uploadResult.result().key
        } catch (storageException: StorageException) {
            Log.e("S3 Upload Error", storageException.localizedMessage, storageException)
            null
        } catch (fileNotFound: FileNotFoundException) {
            Log.e("S3 Upload Error", fileNotFound.localizedMessage, fileNotFound)
            null
        }
    }

    override suspend fun getUrlFromKey(key: String): URL {
        val result = Amplify.Storage.getUrl(key)
        Log.i("S3 Retrieve", "$result")
        return result.url
    }
}