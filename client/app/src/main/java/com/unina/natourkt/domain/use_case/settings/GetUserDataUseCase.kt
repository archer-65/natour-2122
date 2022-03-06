package com.unina.natourkt.domain.use_case.settings

import android.util.Log
import com.unina.natourkt.common.Constants.DATASTORE_STATE
import com.unina.natourkt.domain.model.User
import com.unina.natourkt.domain.repository.PreferencesRepository
import javax.inject.Inject

/**
 * Get logged [User] through [PreferencesRepository]
 */
class GetUserDataUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) {

    suspend operator fun invoke(): User? {
        Log.i(DATASTORE_STATE, "Getting user data attempt...")
        return preferencesRepository.getUserFromDataStore()
    }
}