package com.unina.natourkt.domain.use_case.storage

import android.util.Log
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.storage.StorageException
import com.unina.natourkt.common.convertKeyToUrl
import com.unina.natourkt.domain.repository.StorageRepository
import java.net.URL
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