package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.util.DataState

/**
 * Interface for authentication functions repository
 */
interface AuthRepository {

    /**
     * Fetch authentication session, this function is used only
     * when the app is started
     */
    suspend fun fetchCurrentSession(): Boolean

    /**
     * Provides user registration
     */
    suspend fun register(username: String, email: String, password: String): DataState<Boolean>

    /**
     * Provides user confirmation after successful registration
     */
    suspend fun confirmRegistration(username: String, code: String): DataState<Boolean>

    /**
     * Provides user login
     */
    suspend fun login(username: String, password: String): DataState<Boolean>

    /**
     * Provides user login with socials
     */
    suspend fun login(provider: String): DataState<Boolean>

    /**
     * Resend confirmation code
     */
    suspend fun resendCode(username: String): DataState<Boolean>

    /**
     * Reset password request
     */
    suspend fun resetPasswordRequest(username: String): DataState<Boolean>

    /**
     * Reset password confirmation
     */
    suspend fun resetPasswordConfirm(password: String, code: String): DataState<Boolean>

    /**
     * Logout user
     */
    suspend fun logout(): DataState<Boolean>
}