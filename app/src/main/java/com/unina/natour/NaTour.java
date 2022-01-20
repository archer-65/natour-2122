package com.unina.natour;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

public class NaTour extends Application {
    public void onCreate() {
        super.onCreate();

        try {
            // Cognito
            Amplify.addPlugin(new AWSCognitoAuthPlugin());

            Amplify.configure(getApplicationContext());
            Log.i("NaTourAmplify", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("NaTourAmplify", "Could not initialize Amplify", error);
        }
    }
}
