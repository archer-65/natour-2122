package com.unina.natourkt.core.domain.use_case.settings

import android.util.Log
import com.unina.natourkt.core.util.Constants.DATASTORE_STATE
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.repository.PreferencesRepository
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