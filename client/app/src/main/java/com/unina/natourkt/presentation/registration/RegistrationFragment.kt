package com.unina.natourkt.presentation.registration

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.unina.natourkt.R
import com.unina.natourkt.common.setBottomMargin
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.common.updateText
import com.unina.natourkt.databinding.FragmentRegistrationBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

/**
 * This Fragment represents the SignUp Screen
 */
@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding, RegistrationViewModel>() {

    private val viewModel: RegistrationViewModel by navGraphViewModels(R.id.navigation_auth_flow)

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
            if (isFormValid()) {
                viewModel.registration()
            }
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            usernameTextField.updateText {
                setUsername(it)
            }

            emailTextField.updateText {
                setEmail(it)
            }

            passwordTextField.updateText {
                setPassword(it)
            }

            confirmPasswordTextField.updateText {
                setConfirmPassword(it)
            }
        }
    }


    override fun collectState() = with(binding) {
        with(viewModel) {
            collectOnLifecycleScope(uiRegistrationState) {
                // When the sign up is complete we navigate to the home screen
                if (it.isSignUpComplete) {
                    findNavController().navigate(R.id.action_registration_to_confirmation)
                }

                progressBar.isVisible = it.isLoading

                // When an error is present
                it.errorMessage?.run {
                    manageMessage(this)
                }
            }


            collectOnLifecycleScope(uiRegistrationFormState) {
                signUpButton.isEnabled =
                    it.username.isNotBlank() && it.password.isNotBlank() && it.password.isNotBlank() && it.confirmPassword.isNotBlank()
            }
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        with(binding) {
            with(viewModel.uiRegistrationFormState.value) {
                val isUsernameValid = isUsernameValid.also { valid ->
                    val error = if (!valid) getString(R.string.username_check) else null
                    usernameTextField.error = error
                }

                val isEmailValid = isEmailValid.also { valid ->
                    val error = if (!valid) getString(R.string.email_check) else null
                    emailTextField.error = error
                }

                val isPasswordValid = isPasswordValid.also { valid ->
                    val error = if (!valid) getString(R.string.password_length) else null
                    passwordTextField.error = error
                }

                val isConfirmPasswordValid = isConfirmPasswordValid.also { valid ->
                    val error = if (!valid) getString(R.string.passwords_matching) else null
                    confirmPasswordTextField.error = error
                }

                return isUsernameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
            }
        }
    }
}