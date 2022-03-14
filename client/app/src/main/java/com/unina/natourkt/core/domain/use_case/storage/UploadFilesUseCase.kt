package com.unina.natourkt.core.domain.use_case.storage

import android.net.Uri
import android.util.Log
import com.unina.natourkt.core.util.toInputStream
import com.unina.natourkt.core.domain.repository.StorageRepository
import javax.inject.Inject

class UploadFilesUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
) {

    suspend operator fun invoke(key: String, uri: Uri): String? {
        return try {
            val stream = uri.toInputStream()
            stream.let { storageRepository.uploadFromUri(key, it.getOrThrow()!!) }
        } catch (error: Exception) {
            Log.e("Uploading error", error.localizedMessage, error)
            null
        }
    }
}
