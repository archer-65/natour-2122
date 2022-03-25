package com.unina.natourkt.feature_auth.forgot_password.reset

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.databinding.FragmentResetPasswordBinding
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
        confirmPasswordTextField.onEnter { hideKeyboard() }

        passwordResetButton.setOnClickListener {
            hideKeyboard()
            viewModel.onEvent(ResetPasswordEvent.Reset)
        }
    }

    /**
     * Function to set TextListeners
     */
    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            confirmCodeTextField.updateText {
                onEvent(ResetPasswordEvent.EnteredCode(it))
            }

            passwordTextField.updateText {
                onEvent(ResetPasswordEvent.EnteredPassword(it))
            }

            confirmPasswordTextField.updateText {
                onEvent(ResetPasswordEvent.EnteredConfirmPassword(it))
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectOnLifecycleScope(uiState) {
                // When the password is reset we show a message and navigate to login screen
                if (it.isPasswordReset) {
                    findNavController().navigate(R.id.action_new_password_to_login)
                }

                progressBar.isVisible = it.isLoading
            }

            collectOnLifecycleScope(formState) {
                passwordResetButton.isEnabled = it.isButtonEnabled
                confirmCodeTextField.error = it.code.error?.asString(requireContext())
                passwordTextField.error = it.password.error?.asString(requireContext())
                confirmPasswordTextField.error =
                    it.confirmPassword.error?.asString(requireContext())
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEffect.ShowSnackbar -> {
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