package com.unina.natourkt

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.unina.natourkt.common.Constants.AMPLIFY
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NatourApp : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i(AMPLIFY, "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e(AMPLIFY, "Could not initialize Amplify", error)
        }
    }
}