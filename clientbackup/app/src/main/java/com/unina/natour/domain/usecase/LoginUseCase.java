package com.unina.natour.domain.usecase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.rx.RxAmplify;
import com.unina.natour.common.Resource;
import com.unina.natour.domain.model.User;
import com.unina.natour.ui.RootActivity;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginUseCase {

    @Inject
    LoginUseCase() {
    }

    public Single<Resource<User>> login(String username, String password) {

        User loggedUser = new User();

        return Single.create(emitter -> {
            try {
                emitter.onSuccess(new Resource.Loading<User>());

                RxAmplify.Auth.signIn(username, password)
                        .ignoreElement()
                        .andThen(RxAmplify.Auth.fetchUserAttributes())
                        .subscribe(
                                authUserAttributes -> loggedUser.setUsername(authUserAttributes.get(0).getValue()),
                                error -> Log.e("AuthState", "Failed to fetch attributes")
                        )
                        .dispose();

                emitter.onSuccess(new Resource.Success<User>(loggedUser));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }


//
//    public MutableLiveData<Boolean> login(String username, String password) {
//        MutableLiveData<Boolean> loginState = new MutableLiveData<>();
//
//        Amplify.Auth.signIn(
//                username,
//                password,
//                result -> {
//                    Log.i("AuthQuickstart",
//                            result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
//
//                    loginState.postValue(result.isSignInComplete());
//                },
//                error -> {
//                    Log.e("AuthQuickstart", error.toString());
//
//                    loginState.postValue(false);
//                }
//        );
//
//        return loginState;
//    }
}
