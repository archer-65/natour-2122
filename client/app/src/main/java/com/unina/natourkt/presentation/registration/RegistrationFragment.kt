package com.unina.natourkt.presentation.registration

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.common.Constants.PASSWORD_LENGTH
import com.unina.natourkt.common.DataState
import com.unina.natourkt.databinding.FragmentRegistrationBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This Fragment represents the SignUp Screen
 */
@AndroidEntryPoint
class RegistrationFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
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
     * Start to collect [RegistrationUiState], action based on Success/Loading/Error
     */
    private fun collectState() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationViewModel.uiRegistrationState.collect { uiState ->
                    uiState.apply {
                        // Bind the progress bar visibility
                        progressBar.isVisible = isLoading

                        // When the sign up is complete
                        if (isSignUpComplete) {
                            // We navigate to the home screen
                            findNavController().navigate(R.id.navigation_registration_to_navigation_confirmation)
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
        signUpButton.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        registrationImage.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }
    }

    /**
     * Function to set listeners for views
     */
    private fun setListeners() = with(binding) {
        signUpButton.setOnClickListener {
            if (isFormValid()) {
                registrationViewModel.registration(
                    usernameTextField.editText?.text.toString(),
                    emailTextField.editText?.text.toString(),
                    passwordTextField.editText?.text.toString()
                )
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

        emailTextField.editText?.doAfterTextChanged {
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
        signUpButton.isEnabled = usernameTextField.editText?.text!!.isNotBlank()
                && emailTextField.editText?.text!!.isNotBlank()
                && passwordTextField.editText?.text!!.isNotBlank()
                && confirmPasswordTextField.editText?.text!!.isNotBlank()
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        val isUsernameValid = isUsernameValid()
        val isEmailValid = isEmailValid()
        val isPasswordValid = isPasswordValid()

        return isUsernameValid && isPasswordValid && isEmailValid
    }

    /**
     * Check if the username is valid and manage TextField errors
     */
    private fun isUsernameValid(): Boolean = with(binding) {

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
     * Check if the email is valid and manage TextField errors
     */
    private fun isEmailValid(): Boolean = with(binding) {

        val email = emailTextField.editText?.text!!.trim().toString()
        val match = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        return if (!match) {
            emailTextField.error = getString(R.string.email_check)
            false
        } else {
            emailTextField.error = null
            true
        }
    }

    /**
     * Check if the password is valid and manage TextField errors
     */
    private fun isPasswordValid(): Boolean = with(binding) {

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

    private fun manageMessage(customMessage: DataState.CustomMessages) {
        // Get the right message
        val message = when (customMessage) {
            DataState.CustomMessages.UsernameExists -> getString(R.string.username_exists)
            DataState.CustomMessages.AliasExists -> getString(R.string.credentials_already_taken)
            DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
            DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
            else -> getString(R.string.auth_failed_generic)
        }

        showSnackbar(message)
    }
}