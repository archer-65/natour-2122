package com.unina.natourkt.feature_auth.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.core.util.Constants.FACEBOOK
import com.unina.natourkt.core.util.Constants.GOOGLE
import com.unina.natourkt.databinding.FragmentLoginBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.navigateOnClick
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.core.presentation.util.updateText
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the Login Screen
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setTextChangedListeners()
    }

    override fun setupUi() = with(binding) {
        facebookButton.setBottomMargin()
        loginImage.setTopMargin()
    }

    override fun setListeners() = with(binding) {
        with(viewModel) {
            loginButton.setOnClickListener {
                if (isFormValid()) {
                    login()
                }
            }

            googleButton.setOnClickListener {
                login(GOOGLE)
            }

            facebookButton.setOnClickListener {
                login(FACEBOOK)
            }

            forgotPasswordButton.navigateOnClick(R.id.action_login_to_forgot_password)

            signUpTextView.navigateOnClick(R.id.action_login_to_registration)
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            usernameTextField.updateText {
                setUsername(it)
            }

            passwordTextField.updateText {
                setPassword(it)
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectOnLifecycleScope(uiState) {
                // When the user is logged in we navigate to the home screen
                if (it.isUserLoggedIn) {
                    findNavController().navigate(R.id.navigation_login_to_navigation_home)
                }

                progressBar.isVisible = it.isLoading

                // When an error is present we show the error through snackbar
                it.errorMessage?.run {
                    manageMessage(this)
                }
            }

            collectOnLifecycleScope(formState) {
                loginButton.isEnabled = it.username.isNotBlank() && it.password.isNotBlank()
            }
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        with(binding) {
            with(viewModel.formState.value) {
                val isUsernameValid = isUsernameValid.also { valid ->
                    val error = if (!valid) getString(R.string.username_check) else null
                    usernameTextField.error = error
                }
                val isPasswordValid = isPasswordValid.also { valid ->
                    val error = if (!valid) getString(R.string.password_length) else null
                    passwordTextField.error = error
                }

                return isUsernameValid && isPasswordValid
            }
        }
    }
}