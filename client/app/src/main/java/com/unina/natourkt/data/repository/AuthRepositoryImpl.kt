package com.unina.natourkt.data.repository

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.unina.natourkt.common.Constants.AMPLIFY
import com.unina.natourkt.common.Constants.FACEBOOK
import com.unina.natourkt.common.Constants.GOOGLE
import com.unina.natourkt.domain.repository.AuthRepository

/**
 * This is a remote utility class built on
 * Amplify Auth methods
 */
class AuthRepositoryImpl : AuthRepository {

    /**
     * Fetch authentication session, this function is used only
     * when the app is started
     */
    override suspend fun fetchCurrentSession(): Boolean {

        val session = Amplify.Auth.fetchAuthSession()

        val isSignedIn: Boolean = if (session.isSignedIn) {
            Log.i(AMPLIFY, "$session")
            true
        } else {
            Log.e(AMPLIFY, "Failed to fetch session")
            false
        }

        return isSignedIn
    }

    /**
     * Provides user registration
     */
    override suspend fun register(username: String, email: String, password: String): Boolean {

        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()

        val result = Amplify.Auth.signUp(username, password, options)

        val isSignUpComplete: Boolean = if (result.isSignUpComplete) {
            Log.i(AMPLIFY, "$result")
            true
        } else {
            Log.i(AMPLIFY, "Sign up failed!")
            false
        }

        return isSignUpComplete
    }

    /**
     * Provides user confirmation after successful registration
     */
    override suspend fun confirmRegistration(username: String, code: String): Boolean {

        val result = Amplify.Auth.confirmSignUp(username, code)

        val isSignUpConfirmed: Boolean = if (result.isSignUpComplete) {
            Log.i(AMPLIFY, "$result")
            true
        } else {
            Log.i(AMPLIFY, "Failed to confirm Sign Up.")
            false
        }

        return isSignUpConfirmed
    }

    /**
     * Provides user login
     */
    override suspend fun login(username: String, password: String): Boolean {

        val result = Amplify.Auth.signIn(username, password)

        val isSignInComplete: Boolean = if (result.isSignInComplete) {
            Log.i(AMPLIFY, "$result")
            true
        } else {
            Log.i(AMPLIFY, "Login failed!")
            false
        }

        return isSignInComplete
    }

    /**
     * Provides user login with socials, could be better
     */
    override suspend fun loginSocial(provider: String, activity: FragmentActivity?): Boolean {

        val authProvider: AuthProvider =
            when (provider) {
                FACEBOOK -> AuthProvider.facebook()
                GOOGLE -> AuthProvider.google()
                else -> throw IllegalArgumentException("Illegal provider!")
            }

        val result =
            activity?.let { Amplify.Auth.signInWithSocialWebUI(authProvider, it) }

        val isSignInComplete: Boolean = if (result!!.isSignInComplete) {
            Log.i(AMPLIFY, "$result")
            true
        } else {
            Log.e(AMPLIFY, "Login failed")
            false
        }

        return isSignInComplete
    }
}