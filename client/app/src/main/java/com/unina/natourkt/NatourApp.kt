package com.unina.natourkt

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify

class NatourApp : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())

            Amplify.configure(applicationContext)
            Log.i("Amplify Init", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("Amplify Init", "Could not initialize Amplify", error)
        }
    }
}