package com.unina.natourkt.domain.use_case.storage

import android.net.Uri
import android.util.Log
import com.amplifyframework.storage.StorageException
import com.unina.natourkt.domain.repository.StorageRepository
import com.unina.natourkt.domain.use_case.global.OpenInputStreamUseCase
import java.lang.Exception
import java.net.URL
import javax.inject.Inject

class UploadFilesUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
    private val openInputStreamUseCase: OpenInputStreamUseCase,
) {

    suspend operator fun invoke(key: String, uri: Uri): String? {
        return try {
            val stream = openInputStreamUseCase(uri).getOrThrow()
            stream?.let { storageRepository.uploadFromUri(key, it) }
        } catch (error: Exception) {
            Log.e("Uploading error", error.localizedMessage, error)
            null
        }
    }
}
