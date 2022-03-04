package com.unina.natourkt.domain.use_case.storage

import android.net.Uri
import android.util.Log
import com.amplifyframework.storage.StorageException
import com.unina.natourkt.domain.repository.StorageRepository
import java.net.URL
import javax.inject.Inject

class UploadFilesUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(key: String, uri: Uri): String? {
        return try {
            storageRepository.uploadFromUri(key, uri)
        } catch (error: StorageException) {
            Log.e("S3 error", error.localizedMessage, error)
            null
        }
    }
}
