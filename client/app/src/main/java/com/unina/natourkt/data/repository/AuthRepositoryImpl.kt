package com.unina.natourkt.data.repository

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.step.AuthResetPasswordStep
import com.amplifyframework.kotlin.core.Amplify
import com.unina.natourkt.common.Constants.AMPLIFY
import com.unina.natourkt.common.Constants.FACEBOOK
import com.unina.natourkt.common.Constants.GOOGLE
import com.unina.natourkt.domain.repository.AuthRepository
import com.unina.natourkt.presentation.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * This is a remote utility class built on [Amplify] methods
 */
class AuthRepositoryImpl : AuthRepository {

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
     * Fetch User Sub (Amplify UUID), will be used to query
     * user from REST API
     */
    override suspend fun fetchUserSub(): String {

        val currentUser = Amplify.Auth.getCurrentUser()
        Log.i(AMPLIFY, "$currentUser")

        return currentUser!!.userId
    }

    /**
     * Provides user registration
     */
    override suspend fun register(username: String, email: String, password: String): Boolean {

        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()

        val result = Amplify.Auth.signUp(username, password, options)
        Log.i(AMPLIFY, "$result")

        return result.isSignUpComplete
    }

    /**
     * Provides user confirmation after successful registration
     */
    override suspend fun confirmRegistration(username: String, code: String): Boolean {

        val result = Amplify.Auth.confirmSignUp(username, code)
        Log.i(AMPLIFY, "$result")

        return result.isSignUpComplete
    }

    /**
     * Provides user login
     */
    override suspend fun login(username: String, password: String): Boolean {

        val result = Amplify.Auth.signIn(username, password)
        Log.i(AMPLIFY, "$result")

        return result.isSignInComplete
    }

    /**
     * Provides user login with socials
     */
    override suspend fun login(provider: String): Boolean {

        val authProvider: AuthProvider =
            when (provider) {
                FACEBOOK -> AuthProvider.facebook()
                GOOGLE -> AuthProvider.google()
                else -> throw IllegalArgumentException("Illegal provider!")
            }

        val result = Amplify.Auth.signInWithSocialWebUI(authProvider, MainActivity.instance)
        Log.i(AMPLIFY, "$result")

        return result.isSignInComplete
    }

    /**
     * Resend confirmation code
     */
    override suspend fun resendCode(username: String): Boolean {

        val result = Amplify.Auth.resendSignUpCode(username)
        Log.i(AMPLIFY, "$result")

        return result.isSignUpComplete
    }

    /**
     * Reset password request
     */
    override suspend fun resetPasswordRequest(username: String): Boolean {

        val result = Amplify.Auth.resetPassword(username)
        Log.i(AMPLIFY, "$result")

        return result.nextStep.resetPasswordStep == AuthResetPasswordStep.CONFIRM_RESET_PASSWORD_WITH_CODE
    }
}