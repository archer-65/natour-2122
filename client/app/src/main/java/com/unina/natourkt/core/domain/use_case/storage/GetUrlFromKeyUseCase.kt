package com.unina.natourkt.core.domain.use_case.storage

import android.util.Log
import com.amplifyframework.storage.StorageException
import com.unina.natourkt.core.util.convertKeyToUrl
import com.unina.natourkt.core.domain.repository.StorageRepository
import javax.inject.Inject

class GetUrlFromKeyUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(key: String): String {
        return key.convertKeyToUrl {
            storageConversion(it)
        }
    }

    private suspend fun storageConversion(key: String): String {
        val url = try {
            storageRepository.getUrlFromKey(key)
        } catch (error: StorageException) {
            Log.e("S3 Retrieve Error", error.localizedMessage, error)
            null
        }

        return url.toString()
    }
}