package com.unina.natourkt.domain.repository

import com.unina.natourkt.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface for simple DataStore Preferences methods
 */
interface PreferencesRepository {
    
    suspend fun saveUserToDataStore(user: User)

    suspend fun getUserFromDataStore(): User?
}