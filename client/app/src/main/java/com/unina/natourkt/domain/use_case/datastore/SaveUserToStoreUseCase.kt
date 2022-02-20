package com.unina.natourkt.domain.use_case.datastore

import android.util.Log
import com.unina.natourkt.common.Constants
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.repository.DataStoreRepository
import javax.inject.Inject

/**
 * Save logged [User] through [DataStoreRepository]
 */
class SaveUserToStoreUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
){

    /**
     * Save logged in user
     */
    suspend operator fun invoke(user: User) {
        Log.i(Constants.DATASTORE_STATE, "Saving user data attempt...")
        dataStoreRepository.saveUserToDataStore(user)
    }
}