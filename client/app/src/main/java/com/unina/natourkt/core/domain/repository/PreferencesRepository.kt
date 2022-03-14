package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.domain.model.User

/**
 * Interface for simple DataStore Preferences methods
 */
interface PreferencesRepository {
    
    suspend fun saveUserToDataStore(user: User)

    suspend fun getUserFromDataStore(): User?
}