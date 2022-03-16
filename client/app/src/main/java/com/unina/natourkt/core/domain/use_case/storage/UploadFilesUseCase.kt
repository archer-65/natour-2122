package com.unina.natourkt.core.domain.use_case.storage

import android.net.Uri
import android.util.Log
import com.unina.natourkt.core.util.toInputStream
import com.unina.natourkt.core.domain.repository.StorageRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.FileNotFoundException
import javax.inject.Inject

class UploadFilesUseCase @Inject constructor(
    private val storageRepository: StorageRepository,
) {

    suspend operator fun invoke(key: String, uri: Uri): String? {
        return storageRepository.uploadFromUri(key, uri)
    }
}
