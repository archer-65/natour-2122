package com.unina.natourkt.data.repository

import android.util.Log
import com.amazonaws.mobileconnectors.cognitoauth.Auth
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.step.AuthResetPasswordStep
import com.amplifyframework.kotlin.core.Amplify
import com.unina.natourkt.common.Constants.AMPLIFY
import com.unina.natourkt.common.Constants.FACEBOOK
import com.unina.natourkt.common.Constants.GOOGLE
import com.unina.natourkt.common.Constants.LOGIN_STATE
import com.unina.natourkt.common.Constants.NETWORK_ERROR
import com.unina.natourkt.common.Constants.PASSWORD_RESET
import com.unina.natourkt.common.Constants.REGISTRATION_STATE
import com.unina.natourkt.common.Constants.SERVER_ERROR
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.ErrorHandler
import com.unina.natourkt.data.remote.dto.toUser
import com.unina.natourkt.data.remote.retrofit.UserApi
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.domain.repository.DataStoreRepository
import com.unina.natourkt.presentation.main.MainActivity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * This is an [AuthRepository] class built on [Amplify] methods
 */
class AuthRepositoryImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userApi: UserApi,
) : AuthRepository {

    /**
     * Fetch authentication session, this function is used only
     * when the app is started
     */
    override suspend fun fetchCurrentSession(): Boolean {
        val session = Amplify.Auth.fetchAuthSession()
        Log.i(AMPLIFY, "$session")

        return session.isSignedIn
    }

    /**
     * Provides user registration
     */
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): DataState<Boolean> = try {
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()

        // Try sign up with Amplify
        val result = Amplify.Auth.signUp(username, password, options)
        Log.i(AMPLIFY, "$result")

        if (result.isSignUpComplete) {
            DataState.Success(true)
        } else {
            DataState.Error(DataState.CustomMessage.AuthGeneric)
        }
    } catch (authException: AuthException) {
        Log.e(REGISTRATION_STATE, authException.localizedMessage, authException)
        DataState.Error(ErrorHandler.handleException(authException))
    }


    /**
     * Provides user confirmation after successful registration
     */
    override suspend fun confirmRegistration(username: String, code: String): DataState<Boolean> {
        return try {
            val result = Amplify.Auth.confirmSignUp(username, code)
            Log.i(AMPLIFY, "$result")

            if (result.isSignUpComplete) {
                DataState.Success(true)
            } else {
                DataState.Error(DataState.CustomMessage.AuthGeneric)
            }
        } catch (authException: AuthException) {
            Log.e(REGISTRATION_STATE, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        }
    }

    /**
     * Resend confirmation code
     */
    override suspend fun resendCode(username: String): DataState<Boolean> {
        return try {
            val result = Amplify.Auth.resendSignUpCode(username)
            Log.i(AMPLIFY, "$result")

            if (result.isSignUpComplete) {
                DataState.Success(true)
            } else {
                DataState.Error(DataState.CustomMessage.AuthGeneric)
            }
        } catch (authException: AuthException) {
            Log.e(REGISTRATION_STATE, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        }
    }

    /**
     * Provides user login
     */
    override suspend fun login(username: String, password: String): DataState<Boolean> {
        return try {
            // Try sign in with Amplify
            val result = Amplify.Auth.signIn(username, password)
            Log.i(AMPLIFY, "$result")

            if (result.isSignInComplete) {
                // Fetch sub of logged user
                val sub = Amplify.Auth.getCurrentUser()!!.userId
                // Make request to API to retrieve data
                val user = userApi.getUserByUUID(sub).toUser()
                // Save user data to DataStore Preferences
                dataStoreRepository.saveUserToDataStore(user)
                // Return Success
                DataState.Success(true)
            } else {
                // Return error
                DataState.Error(DataState.CustomMessage.AuthGeneric)
            }
        } catch (authException: AuthException) {
            Log.e(LOGIN_STATE, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        } catch (ioException: IOException) {
            Log.e(SERVER_ERROR, ioException.localizedMessage, ioException)
            DataState.Error(ErrorHandler.handleException(ioException))
        } catch (httpException: HttpException) {
            Log.e(NETWORK_ERROR, httpException.localizedMessage, httpException)
            DataState.Error(ErrorHandler.handleException(httpException))
        }
    }

    /**
     * Provides user login with socials
     */
    override suspend fun login(provider: String): DataState<Boolean> {
        return try {
            val authProvider: AuthProvider =
                when (provider) {
                    FACEBOOK -> AuthProvider.facebook()
                    GOOGLE -> AuthProvider.google()
                    else -> throw IllegalArgumentException("Illegal provider!")
                }

            val result = Amplify.Auth.signInWithSocialWebUI(authProvider, MainActivity.instance)
            Log.i(AMPLIFY, "$result")

            if (result.isSignInComplete) {
                // Fetch sub of logged user
                val sub = Amplify.Auth.getCurrentUser()!!.userId
                // Make request to API to retrieve data
                val user = userApi.getUserByUUID(sub).toUser()
                // Save user data to DataStore Preferences
                dataStoreRepository.saveUserToDataStore(user)
                // Return Success
                DataState.Success(true)
            } else {
                // Return error
                DataState.Error(DataState.CustomMessage.AuthGeneric)
            }
        } catch (authException: AuthException) {
            Log.e(LOGIN_STATE, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        } catch (ioException: IOException) {
            Log.e(SERVER_ERROR, ioException.localizedMessage, ioException)
            DataState.Error(ErrorHandler.handleException(ioException))
        } catch (httpException: HttpException) {
            Log.e(NETWORK_ERROR, httpException.localizedMessage, httpException)
            DataState.Error(ErrorHandler.handleException(httpException))
        }
    }

    /**
     * Reset password request
     */
    override suspend fun resetPasswordRequest(username: String): DataState<Boolean> {
        return try {
            val result = Amplify.Auth.resetPassword(username)
            Log.i(AMPLIFY, "$result")

            if (result.nextStep.resetPasswordStep == AuthResetPasswordStep.CONFIRM_RESET_PASSWORD_WITH_CODE) {
                DataState.Success(true)
            } else {
                DataState.Error(DataState.CustomMessage.AuthGeneric)
            }
        } catch (authException: AuthException) {
            Log.e(PASSWORD_RESET, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        }
    }

    /**
     * Reset password confirmation
     */
    override suspend fun resetPasswordConfirm(password: String, code: String): DataState<Boolean> {
        return try {
            Amplify.Auth.confirmResetPassword(password, code)
            Log.i(AMPLIFY, "Password changed")

            DataState.Success(true)
        } catch (authException: AuthException) {
            Log.e(PASSWORD_RESET, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        }
    }
}