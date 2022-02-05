package com.unina.natour.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.core.Amplify;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.unina.natour.R;
import com.unina.natour.databinding.FragmentLoginBinding;

import org.w3c.dom.Text;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    TextInputLayout usernameField;
    TextInputEditText usernameInput;
    TextInputLayout passwordField;
    TextInputEditText passwordInput;

    Button loginButton;
    Button signupButton;
    ImageButton googleButton;
    ImageButton facebookButton;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        usernameField = binding.textfieldUsername;
        usernameInput = binding.textinputUsername;
        passwordField = binding.textfieldPassword;
        passwordInput = binding.textinputPassword;

        loginButton = binding.buttonLogin;
        signupButton = binding.buttonSignup;
        googleButton = binding.buttonGoogle;
        facebookButton = binding.buttonFacebook;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        setFieldListeners();

        setFormObserver();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                loginViewModel.loginAttempt(username, password);
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Amplify.Auth.signInWithSocialWebUI(AuthProvider.google(),
                        getActivity(),
                        result -> Log.i("AuthQuickstart", result.toString()),
                        error -> Log.e("AuthQuickstart", error.toString()) );
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Amplify.Auth.signInWithSocialWebUI(AuthProvider.facebook(),
                        getActivity(),
                        result -> Log.i("AuthQuickstart", result.toString()),
                        error -> Log.e("AuthQuickstart", error.toString()));
            }
        });
    }

    /**
     * Creates a TextWatcher and add it to username and password fields
     */
    public void setFieldListeners() {

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameInput.getText().toString(),
                        passwordInput.getText().toString());
            }
        };
        usernameInput.addTextChangedListener(afterTextChangedListener);
        passwordInput.addTextChangedListener(afterTextChangedListener);
    }

    /**
     * Sets the FormState observer
     */
    public void setFormObserver() {

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {

            if (loginFormState == null) {
                return;
            }

            // Set Login Button enabled if the form is valid
            loginButton.setEnabled(loginFormState.isDataValid());

            // Set error on username and reset the error if data is valid
            if (loginFormState.getUsernameError() != null) {
                usernameField.setError(getString(loginFormState.getUsernameError()));
            } else {
                usernameField.setError(null);
            }

            // Set error on password and reset the error if data is valid
            if (loginFormState.getPasswordError() != null) {
                passwordField.setError(getString(loginFormState.getPasswordError()));
            } else  {
                passwordField.setError(null);
            }
        });
    }

}