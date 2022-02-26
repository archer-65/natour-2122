package com.unina.natourkt.presentation.forgot_password.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.common.Constants.PASSWORD_LENGTH
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.inVisible
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentResetPasswordBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        collectState()

        setListeners()
        setTextChangedListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Basic settings for UI
     */
    fun setupUi() = with(binding)   {
       passwordResetButton.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

       forgotPasswordImage.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }
    }

    /**
     * Start to collect [ResetPasswordUiState], action based on Success/Loading/Error
     */
    fun collectState() = with(binding) {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                resetPasswordViewModel.uiState.collect { uiState ->
                    uiState.apply {
                        progressBar.isVisible = isLoading

                        // When the password is resetted
                        if (isPasswordReset) {

                            // The progress bar disappears
                            val message =
                                "La password è stata aggiornata con successo! Effettua l'accesso."
                            showSnackbar(message)

                            // We navigate to login screen
                            findNavController().navigate(R.id.navigation_new_password_to_navigation_login)
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
     * Function to set listeners for views
     */
    private fun setListeners() = with(binding) {
        passwordResetButton.setOnClickListener {
            if (isFormValid()) {
                resetPasswordViewModel.resetConfirm(
                    passwordTextField.editText?.text.toString(),
                    confirmCodeTextField.editText?.text.toString()
                )
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

        passwordTextField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }

        confirmPasswordTextField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }
    }

    /**
     * Validate form to enable button
     */
    private fun isFormValidForButton() = with(binding) {
        passwordResetButton.isEnabled = confirmCodeTextField.editText?.text!!.isNotBlank()
                && passwordTextField.editText?.text!!.isNotBlank()
                && confirmPasswordTextField.editText?.text!!.isNotBlank()
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        val isCodeValid = isCodeValid()
        val isPasswordValid = isPasswordValid()

        return isCodeValid && isPasswordValid
    }

    /**
     * Check if the code is valid and manage TextField errors
     */
    private fun isCodeValid(): Boolean = with(binding){
        val code =
            confirmCodeTextField.editText?.text!!.trim().toString()

        return if (code.length != 6) {
            confirmCodeTextField.error = getString(R.string.code_check)
            false
        } else {
            confirmCodeTextField.error = null
            true
        }
    }

    // Check if the password is valid and manage TextField errors
    private fun isPasswordValid(): Boolean = with(binding){

        val password = passwordTextField.editText?.text!!.trim().toString()
        val confirmPassword = confirmPasswordTextField.editText?.text!!.trim().toString()

        return when {
            password.length < PASSWORD_LENGTH -> {
                passwordTextField.error = getString(R.string.password_length)
                false
            }
            password != confirmPassword -> {
                confirmPasswordTextField.error = getString(R.string.passwords_matching)
                passwordTextField.error = null
                false
            }
            else -> {
                passwordTextField.error = null
                confirmPasswordTextField.error = null
                true
            }
        }
    }

    private fun manageMessage(customMessages: DataState.CustomMessages) {
        // Get the right message
        val message = when (customMessages) {
            is DataState.CustomMessages.CodeDelivery -> getString(R.string.error_confirmation_code_deliver)
            is DataState.CustomMessages.CodeMismatch -> getString(R.string.wrong_confirmation_code)
            is DataState.CustomMessages.CodeExpired -> getString(R.string.expired_confirmation_code)
            is DataState.CustomMessages.InvalidPassword -> getString(R.string.invalid_password)
            is DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
            is DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
            else -> getString(R.string.auth_failed_generic)
        }

        // Show a message
        showSnackbar(message)

    }
}