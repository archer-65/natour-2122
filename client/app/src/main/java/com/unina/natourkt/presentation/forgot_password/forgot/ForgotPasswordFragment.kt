package com.unina.natourkt.presentation.forgot_password.forgot

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

    // ViewModel
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
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
     * Start to collect [ForgotPasswordUiState], action based on Success/Loading/Error
     */
    private fun collectState() = with(binding) {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forgotPasswordViewModel.uiState.collect { uiState ->
                    uiState.apply {
                        // Bind the progress bar visibility
                        progressBar.isVisible = isLoading

                        // When the code is sent
                        if (uiState.isCodeSent) {
                            // Show a message
                            val message = "Il codice per il reset della password è stato inviato!"
                            showSnackbar(message)

                            // We navigate to new password insertion screen
                            findNavController().navigate(R.id.navigation_forgot_password_to_navigation_new_password)
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
        forgotPasswordImage.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }
    }

    /**
     * Function to set listeners for views
     */
    private fun setListeners() = with(binding) {
        sendCodeButton.setOnClickListener {
            if (isFormValid()) {
                forgotPasswordViewModel.resetRequest(usernameTextField.editText?.text.toString())
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() = with(binding) {
        usernameTextField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }
    }

    /**
     * Validate form to enable button
     */
    private fun isFormValidForButton() = with(binding) {
        sendCodeButton.isEnabled = usernameTextField.editText?.text!!.isNotBlank()
    }


    /**
     *  Check if the form is Valid
     */
    private fun isFormValid(): Boolean = isValidUsername()

    /**
     * Check if the username is valid and manage TextField errors
     */
    private fun isValidUsername(): Boolean = with(binding) {

        val username = usernameTextField.editText?.text!!.trim().toString()

        return if (username.contains(" ")) {
            usernameTextField.error = getString(R.string.username_check)
            false
        } else {
            usernameTextField.error = null
            true
        }
    }

    /**
     * Just an message-based function
     */
    private fun manageMessage(customMessage: DataState.CustomMessages) {
        // Get the right message
        val message = when (customMessage) {
            is DataState.CustomMessages.UserNotFound -> getString(R.string.user_not_found)
            DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
            else -> getString(R.string.auth_failed_generic)
        }

        showSnackbar(message)
    }
}