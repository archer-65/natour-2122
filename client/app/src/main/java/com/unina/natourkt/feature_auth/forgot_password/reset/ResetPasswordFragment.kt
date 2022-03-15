package com.unina.natourkt.feature_auth.forgot_password.reset

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentResetPasswordBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.core.presentation.util.updateText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding, ResetPasswordViewModel>() {

    private val viewModel: ResetPasswordViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentResetPasswordBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setTextChangedListeners()
    }

    override fun setupUi() = with(binding) {
        forgotPasswordImage.setTopMargin()
        passwordResetButton.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        passwordResetButton.setOnClickListener {
            if (isFormValid()) {
                viewModel.resetConfirm()
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            confirmCodeTextField.updateText {
                setCode(it)
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
            collectOnLifecycleScope(uiState) {
                // When the password is reset we show a message and navigate to login screen
                if (it.isPasswordReset) {
                    val message = getString(R.string.password_reset_success)
                    showSnackbar(message)

                    findNavController().navigate(R.id.action_new_password_to_login)
                }

                progressBar.isVisible = it.isLoading

                // When an error is present we show the error through snackbar
                it.errorMessage?.run {
                    manageMessage(this)
                }
            }

            collectOnLifecycleScope(formState) {
                passwordResetButton.isEnabled =
                    it.code.isNotBlank() && it.password.isNotBlank() && it.confirmPassword.isNotBlank()
            }
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        with(binding) {
            with(viewModel.formState.value) {
                val isCodeValid = isCodeValid.also { valid ->
                    val error = if (!valid) getString(R.string.code_check) else null
                    confirmCodeTextField.error = error
                }

                val isPasswordValid = isPasswordValid.also { valid ->
                    val error = if (!valid) getString(R.string.password_length) else null
                    passwordTextField.error = error
                }

                val isConfirmPasswordValid = isConfirmPasswordValid.also { valid ->
                    val error = if (!valid) getString(R.string.passwords_matching) else null
                    confirmPasswordTextField.error = error
                }

                return isCodeValid && isPasswordValid && isConfirmPasswordValid
            }
        }
    }
}