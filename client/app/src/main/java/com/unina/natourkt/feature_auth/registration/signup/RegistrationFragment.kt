package com.unina.natourkt.feature_auth.registration.signup

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the SignUp Screen
 */
@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding, RegistrationViewModel>() {

    private val viewModel: RegistrationViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRegistrationBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setTextChangedListeners()
    }

    override fun setupUi() = with(binding) {
        registrationImage.setTopMargin()
        signUpButton.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        signUpButton.setOnClickListener {
            viewModel.onEvent(RegistrationEvent.Registration)
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            usernameTextField.updateText {
                onEvent(RegistrationEvent.EnteredUsername(it))
            }

            emailTextField.updateText {
                onEvent(RegistrationEvent.EnteredEmail(it))
            }

            passwordTextField.updateText {
                onEvent(RegistrationEvent.EnteredPassword(it))
            }

            confirmPasswordTextField.updateText {
                onEvent(RegistrationEvent.EnteredConfirmPassword(it))
            }
        }
    }


    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                // When the sign up is complete we navigate to the home screen
                if (it.isSignUpComplete) {
                    val action = RegistrationFragmentDirections.actionRegistrationToConfirmation(
                        formState.value.username.text
                    )
                    findNavController().navigate(action)
                }

                progressBar.isVisible = it.isLoading
            }


            collectOnLifecycleScope(formState) {
                signUpButton.isEnabled = it.isButtonEnabled
                usernameTextField.error = it.username.error?.asString(requireContext())
                emailTextField.error = it.email.error?.asString(requireContext())
                passwordTextField.error = it.password.error?.asString(requireContext())
                confirmPasswordTextField.error =
                    it.confirmPassword.error?.asString(requireContext())
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