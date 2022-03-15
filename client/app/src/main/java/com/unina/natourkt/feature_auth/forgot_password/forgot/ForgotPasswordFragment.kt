package com.unina.natourkt.feature_auth.forgot_password.forgot

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentForgotPasswordBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.core.presentation.util.updateText
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the first screen
 * of a password recover operations
 */
@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding, ForgotPasswordViewModel>() {

    // ViewModel
    private val viewModel: ForgotPasswordViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentForgotPasswordBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setTextChangedListeners()
    }

    override fun setupUi() = with(binding) {
        forgotPasswordImage.setTopMargin()
    }


    override fun setListeners() = with(binding) {
        sendCodeButton.setOnClickListener {
            if (isFormValid()) {
                viewModel.resetRequest()
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    override fun setTextChangedListeners() = with(binding) {
        usernameTextField.updateText {
            viewModel.setUsername(it)
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectOnLifecycleScope(uiState) {
                // When the code is sent show a message, then navigate to new password screen
                if (it.isCodeSent) {
                    val message = getString(R.string.code_resent_password)
                    showSnackbar(message)

                    findNavController().navigate(R.id.action_forgot_password_to_new_password)
                }

                progressBar.isVisible = it.isLoading

                // When an error is present we show the error through snackbar
                it.errorMessage?.run {
                    manageMessage(this)
                }
            }

            collectOnLifecycleScope(formState) {
                // Bind the button visibility
                sendCodeButton.isEnabled = it.username.isNotBlank()
            }
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        with(binding) {
            val isUsernameValid =
                viewModel.formState.value.isUsernameValid.also { valid ->
                    val error = if (!valid) getString(R.string.username_check) else null
                    usernameTextField.error = error
                }

            return isUsernameValid
        }
    }
}