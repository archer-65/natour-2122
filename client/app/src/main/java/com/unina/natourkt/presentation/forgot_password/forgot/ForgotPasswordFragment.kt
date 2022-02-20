package com.unina.natourkt.presentation.forgot_password.forgot

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
import com.unina.natourkt.databinding.FragmentForgotPasswordBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This Fragment represents the first screen
 * of a password recover operations
 */
@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    // Buttons
    private lateinit var sendCodeButton: Button

    // TextFields
    private lateinit var usernameField: TextInputLayout

    // ProgressBar
    private lateinit var progressBar: ProgressBar

    // ViewModel
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUi()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    private fun setupUi() {
        binding.imageForgotPassword.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        sendCodeButton = binding.buttonSendCode

        usernameField = binding.textfieldUsername

        progressBar = binding.progressBar
    }

    /**
     * Start to collect [ForgotPasswordUiState], action based on Success/Loading/Error
     */
    private fun collectState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forgotPasswordViewModel.uiState.collect { uiState ->
                    // When the code is sent
                    if (uiState.isCodeSent) {
                        // The progress bar disappears
                        progressBar.inVisible()

                        // Show a message
                        val message = "Il codice per il reset della password è stato inviato!"
                        showSnackbar(message)

                        // We navigate to new password insertion screen
                        findNavController().navigate(R.id.navigation_forgot_password_to_navigation_new_password)
                    }

                    // When the state loading
                    if (uiState.isLoading) {
                        // Progress bar appears
                        progressBar.visible()
                    }

                    // When an error is present
                    if (uiState.errorMessage != null) {
                        // Get the right message
                        val message = when (uiState.errorMessage) {
                            is DataState.CustomMessages.UserNotFound -> getString(R.string.user_not_found)
                            DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
                            else -> getString(R.string.auth_failed_generic)
                        }

                        // The progress bar disappears
                        progressBar.inVisible()

                        // Show a message
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
        sendCodeButton.setOnClickListener {
            if (isFormValid()) {
                forgotPasswordViewModel.resetRequest(usernameField.editText?.text.toString())
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() {
        usernameField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }
    }

    /**
     * Validate form to enable button
     */
    private fun isFormValidForButton() {
        sendCodeButton.isEnabled = usernameField.editText?.text!!.isNotBlank()
    }


    /**
     *  Check if the form is Valid
     */
    private fun isFormValid(): Boolean {
        val isUsernameValid = isValidUsername()

        return isUsernameValid
    }

    /**
     * Check if the username is valid and manage TextField errors
     */
    private fun isValidUsername(): Boolean {

        val username = usernameField.editText?.text!!.trim().toString()

        return if (username.contains(" ")) {
            usernameField.error = "Lo username non può contenere spazi."
            false
        } else {
            usernameField.error = null
            true
        }
    }
}