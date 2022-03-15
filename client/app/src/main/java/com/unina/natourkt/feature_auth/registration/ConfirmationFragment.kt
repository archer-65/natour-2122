package com.unina.natourkt.feature_auth.registration

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentConfirmationBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.core.presentation.util.updateText
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the Confirmation after Sign Up Screen
 */
@AndroidEntryPoint
class ConfirmationFragment : BaseFragment<FragmentConfirmationBinding, RegistrationViewModel>() {

    private val viewModel: RegistrationViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentConfirmationBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setTextChangedListeners()
    }

    override fun setupUi() = with(binding) {
        confirmationImage.setTopMargin()
    }

    override fun setListeners() = with(binding) {
        with(viewModel) {
            confirmationButton.setOnClickListener {
                if (isFormValid()) {
                    confirmation()
                }
            }

            resendCodeButton.setOnClickListener {
                resendCode()
            }
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            confirmCodeTextField.updateText {
                setCode(it)
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectOnLifecycleScope(uiConfirmationState) {
                // When the user is confirmed a message is shown and we navigate to the login screen
                if (it.isConfirmationComplete) {
                    val message = getString(R.string.confirmed_account)
                    showSnackbar(message)

                    findNavController().navigate(R.id.action_confirmation_to_login)
                }

                // When the code is resent a message is shown
                if (it.isCodeResent) {
                    val message = getString(R.string.code_resent)
                    showSnackbar(message)
                }

                progressBar.isVisible = it.isLoading

                // When an error is present
                it.errorMessage?.run {
                    manageMessage(this)
                }
            }

            collectOnLifecycleScope(uiConfirmationFormState) {
                confirmationButton.isEnabled = it.code.isNotBlank()
            }
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean = with(binding) {
        val isCodeValid =
            viewModel.uiConfirmationFormState.value.isCodeValid.also { valid ->
                val error = if (!valid) getString(R.string.code_check) else null
                confirmCodeTextField.error = error
            }

        return isCodeValid
    }
}