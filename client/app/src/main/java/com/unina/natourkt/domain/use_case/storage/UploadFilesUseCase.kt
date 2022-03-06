package com.unina.natourkt.domain.use_case.storage

import android.net.Uri
import android.util.Log
import com.unina.natourkt.common.toInputStream
import com.unina.natourkt.domain.repository.StorageRepository
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
