package com.unina.natourkt.core.data.repository

import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.step.AuthResetPasswordStep
import com.amplifyframework.kotlin.core.Amplify
import com.unina.natourkt.core.data.remote.dto.mapper.UserApiMapper
import com.unina.natourkt.core.data.remote.retrofit.UserApi
import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.domain.repository.PreferencesRepository
import com.unina.natourkt.core.presentation.main.MainActivity
import com.unina.natourkt.core.util.Constants.AMPLIFY
import com.unina.natourkt.core.util.Constants.FACEBOOK
import com.unina.natourkt.core.util.Constants.GOOGLE
import com.unina.natourkt.core.util.Constants.LOGIN_STATE
import com.unina.natourkt.core.util.Constants.NETWORK_ERROR
import com.unina.natourkt.core.util.Constants.PASSWORD_RESET
import com.unina.natourkt.core.util.Constants.REGISTRATION_STATE
import com.unina.natourkt.core.util.Constants.SERVER_ERROR
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.ErrorHandler
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * This is an [AuthRepository] class built on [Amplify] methods
 */
class AuthRepositoryImpl @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val userApiMapper: UserApiMapper,
    private val userApi: UserApi,
) : AuthRepository {

    /**
     * Gets AWS Cognito JWT
     */
    private fun getToken(): Boolean {
        val groups = ArrayList<String>()
        val GROUP_KEY = "cognito:groups"
        val token = AWSMobileClient.getInstance().tokens.accessToken.tokenString
        val payload = CognitoJWTParser.getPayload(token)
        if (payload.has(GROUP_KEY)) {
            val jsonGroups = payload.getJSONArray(GROUP_KEY)
            for (i in 0..jsonGroups.length() - 1) {
                groups.add(jsonGroups.getString(i))
            }
        }
        return groups.contains("admins")
    }

    override suspend fun fetchCurrentSession(): Boolean {
        val session = Amplify.Auth.fetchAuthSession()
        Log.i(AMPLIFY, "$session")

        return session.isSignedIn
    }

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
            DataState.Error(DataState.Cause.AuthGeneric)
        }
    } catch (authException: AuthException) {
        Log.e(REGISTRATION_STATE, authException.localizedMessage, authException)
        DataState.Error(ErrorHandler.handleException(authException))
    }

    override suspend fun confirmRegistration(username: String, code: String): DataState<Boolean> {
        return try {
            val result = Amplify.Auth.confirmSignUp(username, code)
            Log.i(AMPLIFY, "$result")

            if (result.isSignUpComplete) {
                DataState.Success(true)
            } else {
                DataState.Error(DataState.Cause.AuthGeneric)
            }
        } catch (authException: AuthException) {
            Log.e(REGISTRATION_STATE, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        }
    }

    override suspend fun resendCode(username: String): DataState<Boolean> {
        return try {
            val result = Amplify.Auth.resendSignUpCode(username)
            Log.i(AMPLIFY, "$result")

            if (result.isSignUpComplete) {
                DataState.Success(true)
            } else {
                DataState.Error(DataState.Cause.AuthGeneric)
            }
        } catch (authException: AuthException) {
            Log.e(REGISTRATION_STATE, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        }
    }

    override suspend fun login(username: String, password: String): DataState<Boolean> {
        return try {
            // Try sign in with Amplify
            val result = Amplify.Auth.signIn(username, password)
            Log.i(AMPLIFY, "$result")

            if (result.isSignInComplete) {
                // Fetch sub of logged user
                val sub = Amplify.Auth.getCurrentUser()!!.userId
                // Make request to API to retrieve data
                val userApi = userApi.getUserByUUID(sub)
                val user = userApiMapper.mapToDomain(userApi)
                val isAdmin = getToken()
                val userFinal = user.copy(isAdmin = isAdmin)

                // Save user data to DataStore Preferences
                preferencesRepository.saveUserToPreferences(userFinal)
                // Return Success
                DataState.Success(true)
            } else {
                // Return error
                DataState.Error(DataState.Cause.AuthGeneric)
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
                val userApi = userApi.getUserByUUID(sub)
                val user = userApiMapper.mapToDomain(userApi)
                val isAdmin = getToken()
                val userFinal = user.copy(isAdmin = isAdmin)

                // Save user data to DataStore Preferences
                preferencesRepository.saveUserToPreferences(userFinal)
                // Return Success
                DataState.Success(true)
            } else {
                // Return error
                DataState.Error(DataState.Cause.AuthGeneric)
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

    override suspend fun resetPasswordRequest(username: String): DataState<Boolean> {
        return try {
            val result = Amplify.Auth.resetPassword(username)
            Log.i(AMPLIFY, "$result")

            if (result.nextStep.resetPasswordStep == AuthResetPasswordStep.CONFIRM_RESET_PASSWORD_WITH_CODE) {
                DataState.Success(true)
            } else {
                DataState.Error(DataState.Cause.AuthGeneric)
            }
        } catch (authException: AuthException) {
            Log.e(PASSWORD_RESET, authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        }
    }

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

    override suspend fun logout(): DataState<Boolean> {
        return try {
            Amplify.Auth.signOut()
            Log.i(AMPLIFY, "Logged out")

            DataState.Success(true)
        } catch (authException: AuthException) {
            Log.e("LOGOUT ERROR", authException.localizedMessage, authException)
            DataState.Error(ErrorHandler.handleException(authException))
        }
    }
}