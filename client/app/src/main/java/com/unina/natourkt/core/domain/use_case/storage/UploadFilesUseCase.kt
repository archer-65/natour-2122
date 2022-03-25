package com.unina.natourkt.core.domain.use_case.storage

import android.net.Uri
import android.util.Log
import com.unina.natourkt.core.domain.repository.StorageRepository
import com.unina.natourkt.core.util.Constants
import javax.inject.Inject

/**
 * This UseCase is used to update files
 * @see [StorageRepository]
 */
class UploadFilesUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
) {

    suspend operator fun invoke(key: String, uri: Uri): String? {
        Log.i(Constants.STORAGE, "Trying to update file from uri ${uri} with key ${key}...")

        return storageRepository.uploadFromUri(key, uri)
    }
}
