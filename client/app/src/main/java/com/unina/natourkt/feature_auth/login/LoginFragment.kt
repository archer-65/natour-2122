package com.unina.natourkt.feature_auth.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.util.Constants.FACEBOOK
import com.unina.natourkt.core.util.Constants.GOOGLE
import com.unina.natourkt.databinding.FragmentLoginBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
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
            passwordTextField.onEnter { hideKeyboard() }

            loginButton.setOnClickListener {
                onEvent(LoginEvent.Login)
            }

            googleButton.setOnClickListener {
                onEvent(LoginEvent.LoginSocial(GOOGLE))
            }

            facebookButton.setOnClickListener {
                onEvent(LoginEvent.LoginSocial(FACEBOOK))
            }

            forgotPasswordButton.navigateOnClick(R.id.action_login_to_forgot_password)

            signUpTextView.navigateOnClick(R.id.action_login_to_registration)
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            usernameTextField.updateText {
                onEvent(LoginEvent.EnteredUsername(it))
            }

            passwordTextField.updateText {
                onEvent(LoginEvent.EnteredPassword(it))
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                // When the user is logged in we navigate to the home screen
                if (it.isUserLoggedIn) {
                    findNavController().navigate(R.id.action_login_to_home)
                }

                progressBar.isVisible = it.isLoading
            }

            collectOnLifecycleScope(formState) {
                usernameTextField.error = it.username.error?.asString(requireContext())
                passwordTextField.error = it.password.error?.asString(requireContext())
                loginButton.isEnabled = it.isButtonEnabled
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        Snackbar.make(
                            requireView(),
                            event.uiText.asString(requireContext()),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
                }
            }
        }
    }
}