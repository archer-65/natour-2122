package com.unina.natourkt.domain.use_case.datastore

import android.util.Log
import com.unina.natourkt.common.Constants.DATASTORE_STATE
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.repository.DataStoreRepository
import javax.inject.Inject

/**
 * Get logged [User] through [DataStoreRepository]
 */
class GetUserFromStoreUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {

    suspend operator fun invoke(): User? {
        Log.i(DATASTORE_STATE, "Getting user data attempt...")
        return dataStoreRepository.getUserFromDataStore()
    }
}