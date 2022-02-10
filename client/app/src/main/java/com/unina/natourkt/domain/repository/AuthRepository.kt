package com.unina.natourkt.domain.repository

import androidx.fragment.app.FragmentActivity

interface AuthRepository {

    /**
     * Fetch authentication session, this function is used only
     * when the app is started
     */
    suspend fun fetchCurrentSession(): Boolean

    /**
     * Fetch User Sub (Amplify UUID), will be used to query
     * user from REST API
     */
    suspend fun fetchUserSub(): String

    /**
     * Provides user registration
     */
    suspend fun register(username: String, email: String, password: String): Boolean

    /**
     * Provides user confirmation after successful registration
     */
    suspend fun confirmRegistration(username: String, code: String): Boolean

    /**
     * Provides user login
     */
    suspend fun login(username: String, password: String): Boolean

    /**
     * Provides user login with socials, could be better
     */
    suspend fun loginSocial(provider: String, activity: FragmentActivity): Boolean
}