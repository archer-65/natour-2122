package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.domain.model.User

/**
 * Interface for simple Preferences methods
 */
interface PreferencesRepository {

    /**
     * This function saves logged user information to preferences
     */
    suspend fun saveUserToPreferences(user: User)

    /**
     * This function gets logged user information from preferences
     */
    suspend fun getUserFromPreferences(): User?
}