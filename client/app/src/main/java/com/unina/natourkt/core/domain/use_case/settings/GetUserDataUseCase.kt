package com.unina.natourkt.core.domain.use_case.settings

import android.util.Log
import com.unina.natourkt.core.domain.model.User
import com.unina.natourkt.core.domain.repository.PreferencesRepository
import com.unina.natourkt.core.util.Constants.DATASTORE_STATE
import javax.inject.Inject

/**
 * This UseCase is used to retrieve post details
 * @see [PreferencesRepository]
 */
class GetUserDataUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) {

    suspend operator fun invoke(): User? {
        Log.i(DATASTORE_STATE, "Getting user data attempt...")
        return preferencesRepository.getUserFromPreferences()
    }
}