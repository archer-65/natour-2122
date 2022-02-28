package com.unina.natourkt.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRegistrationBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

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
    private val registrationViewModel: RegistrationViewModel by navGraphViewModels(R.id.navigation_auth_flow)

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
        with(registrationViewModel) {
            launchOnLifecycleScope {
                uiRegistrationState.collect {
                    // When the sign up is complete
                    if (it.isSignUpComplete) {
                        // We navigate to the home screen
                        findNavController().navigate(R.id.navigation_registration_to_navigation_confirmation)
                    }

                    // Bind the progress bar visibility
                    progressBar.isVisible = it.isLoading

                    // When an error is present
                    it.errorMessage?.run {
                        manageMessage(this)
                    }
                }
            }

            launchOnLifecycleScope {
                uiRegistrationFormState.collectLatest {
                    // Bind the button visibility
                    signUpButton.isEnabled =
                        it.username.isNotBlank() &&
                                it.password.isNotBlank() &&
                                it.password.isNotBlank() &&
                                it.confirmPassword.isNotBlank()
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
        with(registrationViewModel) {
            usernameTextField.editText?.doAfterTextChanged {
                val username = it.toString().trim()
                setUsername(username)
            }

            emailTextField.editText?.doAfterTextChanged {
                val email = it.toString().trim()
                setEmail(email)
            }

            passwordTextField.editText?.doAfterTextChanged {
                val password = it.toString().trim()
                setPassword(password)
            }

            confirmPasswordTextField.editText?.doAfterTextChanged {
                val password = it.toString().trim()
                setConfirmPassword(password)
            }
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean = with(binding) {
        val isUsernameValid =
            registrationViewModel.uiRegistrationFormState.value.isUsernameValid.also { valid ->
                val error = if (!valid) getString(R.string.username_check) else null
                usernameTextField.error = error
            }

        val isEmailValid =
            registrationViewModel.uiRegistrationFormState.value.isEmailValid.also { valid ->
                val error = if (!valid) getString(R.string.email_check) else null
                emailTextField.error = error
            }

        val isPasswordValid =
            registrationViewModel.uiRegistrationFormState.value.isPasswordValid.also { valid ->
                val error = if (!valid) getString(R.string.password_length) else null
                passwordTextField.error = error
            }

        val isConfirmPasswordValid =
            registrationViewModel.uiRegistrationFormState.value.isConfirmPasswordValid.also { valid ->
                val error = if (!valid) getString(R.string.passwords_matching) else null
                confirmPasswordTextField.error = error
            }

        return isUsernameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
    }
}