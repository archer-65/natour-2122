package com.unina.natourkt.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.inVisible
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentConfirmationBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This Fragment represents the Confirmation after Sign Up Screen
 */
@AndroidEntryPoint
class ConfirmationFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        setTextChangedListeners()
        collectState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Start to collect LoginState, action based on Success/Loading/Error
     */
    private fun collectState() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationViewModel.uiConfirmationState.collect { uiState ->
                    uiState.apply {
                        // Bind the progress bar visibility
                        progressBar.isVisible = isLoading

                        // When the user is confirmed
                        if (isConfirmationComplete) {
                            // Message is shown
                            val message = getString(R.string.confirmed_account)
                            showSnackbar(message)

                            // We navigate to the login screen
                            findNavController().navigate(R.id.navigation_confirmation_to_navigation_login)
                        }

                        // When the code is resent
                        if (isCodeResent) {
                            // Message is shown
                            val message = getString(R.string.code_resent)
                            showSnackbar(message)
                        }

                        // When an error is present
                        errorMessage?.run {
                            manageMessage(this)
                        }
                    }
                }
            }
        }
    }

    /**
     * Basic settings for UI
     */
    private fun setupUi() = with(binding) {
        confirmationImage.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }
    }

    /**
     * Function to set listeners for views
     */
    private fun setListeners() = with(binding) {
        resendCodeButton.setOnClickListener {
            registrationViewModel.resendCode()
        }

        confirmationButton.setOnClickListener {
            if (isFormValid()) {
                registrationViewModel.confirmation(confirmCodeTextField.editText?.text.toString())
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() = with(binding) {
        confirmCodeTextField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }
    }

    /**
     * Validate form to enable button
     */
    private fun isFormValidForButton() = with(binding) {
        confirmationButton.isEnabled = confirmCodeTextField.editText?.text!!.isNotBlank()
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean = isCodeValid()

    /**
     * Check if the code is valid and manage TextField errors
     */
    private fun isCodeValid(): Boolean = with(binding) {
        val code = confirmCodeTextField.editText?.text!!.trim().toString()

        return if (code.length != 6) {
            confirmCodeTextField.error = getString(R.string.code_check)
            false
        } else {
            confirmCodeTextField.error = null
            true
        }
    }

    private fun manageMessage(customMessage: DataState.CustomMessages) {
        // Get the right message
        val message = when (customMessage) {
            DataState.CustomMessages.CodeDelivery -> getString(R.string.error_confirmation_code_deliver)
            DataState.CustomMessages.CodeMismatch -> getString(R.string.wrong_confirmation_code)
            DataState.CustomMessages.CodeExpired -> getString(R.string.expired_confirmation_code)
            DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
            else -> getString(R.string.auth_failed_generic)
        }

        showSnackbar(message)
    }
}