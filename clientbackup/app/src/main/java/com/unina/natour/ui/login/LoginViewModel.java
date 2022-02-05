package com.unina.natour.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.unina.natour.R;
import com.unina.natour.common.Resource;
import com.unina.natour.domain.model.User;
import com.unina.natour.domain.usecase.LoginUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    private MutableLiveData<User> userState = new MutableLiveData<>();

    private final LoginUseCase loginUseCase;

    @Inject
    LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<User> getUserState() {
        return userState;
    }

//    public void login(String username, String password) {
//
//        loginUseCase.login(username, password)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        userResource -> {
//                            if (userResource instanceof Resource.Success) {
//                                userState.setValue(((Resource.Success<User>) userResource).data);
//                            } else {
//                                // TODO
//                            }
//                        },
//                        throwable -> {
//                            userState.setValue();
//                        }
//                );
//    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 6;
    }
}