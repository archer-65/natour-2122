package com.unina.natour.domain;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.core.Amplify;
import com.unina.natour.ui.RootActivity;

import javax.inject.Inject;

public class LoginUseCase {

    @Inject
    LoginUseCase() {
    }

    public MutableLiveData<Boolean> login(String username, String password) {
        MutableLiveData<Boolean> loginState = new MutableLiveData<>();

        Amplify.Auth.signIn(
                username,
                password,
                result -> {
                    Log.i("AuthQuickstart",
                            result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");

                    loginState.postValue(result.isSignInComplete());
                },
                error -> {
                    Log.e("AuthQuickstart", error.toString());

                    loginState.postValue(false);
                }
        );

        return loginState;
    }
}
