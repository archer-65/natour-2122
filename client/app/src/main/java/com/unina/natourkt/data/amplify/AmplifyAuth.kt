package com.unina.natourkt.data.remote.repository.data

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.unina.natourkt.MainActivity


/**
 * This is a remote utility class built on
 * Amplify Auth methods
 */
class AmplifyAuth() {

    suspend fun fetchCurrentSession(): Boolean? {

        val isSignedIn: Boolean? = try {
            val session = Amplify.Auth.fetchAuthSession()
            Log.i("Amplify Current Session: ", "$session")

            session.isSignedIn
        } catch (error: AuthException) {
            Log.e("Amplify Current Session: ", "Failed to fetch session", error)

            null
        }

        return isSignedIn
    }

    suspend fun register(username: String, email: String, password: String): Boolean {

        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build();

        val result = Amplify.Auth.signUp(username, password, options)

        val isSignUpComplete: Boolean = if (result.isSignUpComplete) {
            Log.i("Amplify Sign Up: ", "Sign up successful!")
            true
        } else {
            Log.i("Amplify Sign Up: ", "Sign up failed!")
            false
        }

        return isSignUpComplete
    }

    suspend fun confirmRegistration(username: String, code: String): Boolean {

        val result = Amplify.Auth.confirmSignUp(username, code)

        val isSignUpConfirmed: Boolean = if (result.isSignUpComplete) {
            Log.i("Amplify Confirmation: ", "Sign Up confirmed!")
            true
        } else {
            Log.i("Amplify Confirmation", "Failed to confirm Sign Up.")
            false
        }

        return isSignUpConfirmed
    }

    suspend fun login(username: String, password: String): Boolean {

        val result = Amplify.Auth.signIn(username, password)

        val isSignInComplete: Boolean = if (result.isSignInComplete) {
            Log.i("Amplify Login: ", "Login successful!")
            true
        } else {
            Log.i("Amplify Login: ", "Login failed!")
            false
        }

        return isSignInComplete
    }


    suspend fun loginSocial(provider: String, activity: FragmentActivity?): Boolean? {

        val authProvider: AuthProvider =
            when (provider) {
                "FACEBOOK" -> AuthProvider.facebook()
                "GOOGLE" -> AuthProvider.google()
                else -> throw IllegalArgumentException("Illegal provider!")
            }

        val isSignInComplete: Boolean? = try {
            val result =
                activity?.let { Amplify.Auth.signInWithSocialWebUI(authProvider, it) }
            Log.i("Amplify Login: ", "$result")

            result?.isSignInComplete
        } catch (error: AuthException) {
            Log.e("Amplify Login: ", "Login failed", error)

            null
        }

        return isSignInComplete
    }
}