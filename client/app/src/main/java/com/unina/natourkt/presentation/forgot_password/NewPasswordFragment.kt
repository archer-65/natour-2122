package com.unina.natourkt.presentation.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.inVisible
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentNewPasswordBinding
import com.unina.natourkt.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewPasswordFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!

    // Buttons
    private lateinit var resetPasswordButton: Button

    // TextFields
    private lateinit var confirmCodeField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var passwordConfirmField: TextInputLayout

    // ProgressBar
    private lateinit var progressBar: ProgressBar

    private val newPasswordViewModel: NewPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUi()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()

        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Basic settings for UI
     */
    fun setupUi() {
        binding.buttonResetPassword.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        binding.imageForgotPassword.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        resetPasswordButton = binding.buttonResetPassword

        confirmCodeField = binding.textfieldConfirmCodeReset
        passwordField = binding.textfieldPassword
        passwordConfirmField = binding.textfieldConfirmPassword

        progressBar = binding.progressBar
    }

    fun collectState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newPasswordViewModel.uiState.collect { uiState ->
                    if (uiState.isPasswordReset) {
                        // When the password is resetted, navigate to login
                        progressBar.inVisible()
                        val message =
                            "La password è stata aggiornata con successo! Effettua l'accesso."
                        showSnackbar(message)
                        findNavController().navigate(R.id.navigation_new_password_to_navigation_login)
                    }
                    if (uiState.isLoading) {
                        // While loading display progress
                        progressBar.visible()
                    }
                    if (uiState.errorMessage != null) {
                        // When there's an error the progress bar disappears and
                        // a message is displayed
                        val message = when (uiState.errorMessage) {
                            is DataState.CustomMessages.CodeDelivery -> getString(R.string.error_confirmation_code_deliver)
                            is DataState.CustomMessages.CodeMismatch -> getString(R.string.wrong_confirmation_code)
                            is DataState.CustomMessages.CodeExpired -> getString(R.string.expired_confirmation_code)
                            is DataState.CustomMessages.InvalidPassword -> getString(R.string.invalid_password)
                            is DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
                            is DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
                            else -> getString(R.string.auth_failed_generic)
                        }
                        // When there's an error the progress bar disappears and
                        // a message is displayed
                        progressBar.inVisible()
                        showSnackbar(message)
                    }
                }
            }
        }
    }

    /**
     * Function to set listeners for views
     */
    private fun setListeners() {
        resetPasswordButton.setOnClickListener {
            newPasswordViewModel.resetConfirm(
                passwordField.editText?.text.toString(),
                confirmCodeField.editText?.text.toString()
            )
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() {
        confirmCodeField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }

        passwordField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }

        passwordConfirmField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }
    }

    /**
     * Validate form to enable button
     */
    private fun isFormValidForButton() {
        resetPasswordButton.isEnabled = confirmCodeField.editText?.text!!.isNotBlank()
                && passwordField.editText?.text!!.isNotBlank()
                && passwordConfirmField.editText?.text!!.isNotBlank()
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        val isCodeValid = isCodeValid()
        val isPasswordValid = isPasswordValid()

        return isCodeValid && isPasswordValid
    }

    private fun isCodeValid(): Boolean {
        val code =
            confirmCodeField.editText?.text!!.trim().toString()

        return if (code.length != 6) {
            confirmCodeField.error = "Il codice deve contenere 6 cifre"
            false
        } else {
            confirmCodeField.error = null
            true
        }
    }

    private fun isPasswordValid(): Boolean {

        val password = passwordField.editText?.text!!.trim().toString()
        val confirmPassword = passwordConfirmField.editText?.text!!.trim().toString()

        return when {
            password.length < 7 -> {
                passwordField.error = "La password deve contenere almeno 7 caratteri."
                false
            }
            password != confirmPassword -> {
                passwordConfirmField.error = "La password non corrisponde."
                passwordField.error = null
                false
            }
            else -> {
                passwordField.error = null
                passwordConfirmField.error = null
                true
            }
        }
    }
}