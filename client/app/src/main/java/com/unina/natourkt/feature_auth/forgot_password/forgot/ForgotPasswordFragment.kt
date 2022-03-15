package com.unina.natourkt.feature_auth.forgot_password.forgot

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentForgotPasswordBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.asString
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
            viewModel.onEvent(ForgotPasswordEvent.Reset)
        }
    }

    /**
     * Function to set TextListeners
     */
    override fun setTextChangedListeners() = with(binding) {
        usernameTextField.updateText {
            viewModel.onEvent(ForgotPasswordEvent.EnteredUsername(it))
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectOnLifecycleScope(uiState) {
                // When the code is sent show a message, then navigate to new password screen
                if (it.isCodeSent) {
                    findNavController().navigate(R.id.action_forgot_password_to_new_password)
                }

                progressBar.isVisible = it.isLoading
            }

            collectOnLifecycleScope(formState) {
                // Bind the button visibility
                sendCodeButton.isEnabled = it.isButtonEnabled
                usernameTextField.error = it.username.error?.asString(requireContext())
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