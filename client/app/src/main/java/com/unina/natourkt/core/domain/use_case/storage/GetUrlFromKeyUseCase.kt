package com.unina.natourkt.core.domain.use_case.storage

import android.util.Log
import com.amplifyframework.storage.StorageException
import com.unina.natourkt.core.domain.repository.StorageRepository
import com.unina.natourkt.core.util.Constants.STORAGE
import com.unina.natourkt.core.util.convertKeyToUrl
import javax.inject.Inject

/**
 * This UseCase is used to retrieve url of file resource from a key received by backend
 * @see [StorageRepository]
 */
class GetUrlFromKeyUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {

    suspend operator fun invoke(key: String): String {
        return key.convertKeyToUrl {
            storageConversion(it)
        }
    }

    /**
     * This method is used to effectively retrieve the url, the other one, called by `ViewModels`
     * is only the facade of this UseCase
     */
    private suspend fun storageConversion(key: String): String {
        val url = try {
            storageRepository.getUrlFromKey(key)
        } catch (error: StorageException) {
            Log.e(STORAGE, error.localizedMessage, error)
            null
        }

        return url.toString()
    }
}