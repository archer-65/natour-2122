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
class AmplifyAuthDataSource() {

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

    suspend fun registerUser(username: String, email: String, password: String): Boolean? {

        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build();

        val isSignUpComplete: Boolean? = try {
            val result = Amplify.Auth.signUp(username, password, options)
            Log.i("Amplify Sign Up: ", "$result")

            result.isSignUpComplete
        } catch (error: AuthException) {
            Log.e("Auth Sign Up: ", "Failed to Sign Up", error)

            null
        }

        return isSignUpComplete
    }

    suspend fun confirmUser(username: String, code: String): Boolean? {

        val isSignUpConfirmed: Boolean? = try {
            val result = Amplify.Auth.confirmSignUp(username, code)
            Log.i("Amplify Confirmation: ", "$result")

            result.isSignUpComplete
        } catch (error: AuthException) {
            Log.e("Amplify Confirmation", "Failed to confirm Sign Up", error)

            null
        }

        return isSignUpConfirmed
    }

    suspend fun login(username: String, password: String): Boolean? {

        val isSignInComplete: Boolean? = try {
            val result = Amplify.Auth.signIn(username, password)
            Log.i("Amplify Login: ", "$result")

            result.isSignInComplete
        } catch (error: AuthException) {
            Log.e("Amplify Login: ", "Login failed", error)

            null
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