package com.unina.natourkt.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.common.*
import com.unina.natourkt.common.Constants.FACEBOOK
import com.unina.natourkt.common.Constants.GOOGLE
import com.unina.natourkt.common.Constants.PASSWORD_LENGTH
import com.unina.natourkt.databinding.FragmentLoginBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This Fragment represents the Login Screen
 */
@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
     * Start to collect [LoginUiState], action based on Success/Loading/Error
     */
    private fun collectState() = with(binding) {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collect { uiState ->
                    uiState.apply {
                        // Bind the progress bar visibility
                        progressBar.isVisible = isLoading

                        // When the user is logged in
                        if (isUserLoggedIn) {
                            // We navigate to the home screen
                            findNavController().navigate(R.id.navigation_login_to_navigation_home)
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

        facebookButton.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        loginImage.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }
    }

    /**
     * Function to set listeners for views
     */
    private fun setListeners() = with(binding) {
        loginButton.setOnClickListener {
            if (isFormValid()) {
                loginViewModel.login(
                    usernameTextField.editText?.text.toString(),
                    passwordTextField.editText?.text.toString(),
                )
            }
        }

        forgotPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_login_to_navigation_forgot_password)
        }

        signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.navigation_login_to_navigation_registration)
        }

        googleButton.setOnClickListener {
            loginViewModel.login(GOOGLE)
        }

        facebookButton.setOnClickListener {
            loginViewModel.login(FACEBOOK)
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() = with(binding) {
        usernameTextField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }

        passwordTextField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }
    }

    /**
     * Validate form to enable button
     */
    private fun isFormValidForButton() = with(binding) {
        loginButton.isEnabled =
            usernameTextField.editText?.text!!.isNotBlank() && passwordTextField.editText?.text!!.isNotBlank()
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        val isUsernameValid = isUsernameValid()
        val isPasswordValid = isPasswordValid()

        return isUsernameValid && isPasswordValid
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
     * Check if the password is valid and manage TextField errors
     */
    private fun isPasswordValid(): Boolean = with(binding) {

        val password = passwordTextField.editText?.text!!.trim().toString()

        return if (password.length < PASSWORD_LENGTH) {
            passwordTextField.error = getString(R.string.password_length)
            false
        } else {
            passwordTextField.error = null
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
            is DataState.CustomMessages.UserNotConfirmed -> getString(R.string.user_not_confirmed)
            is DataState.CustomMessages.InvalidPassword -> getString(R.string.invalid_password)
            is DataState.CustomMessages.InvalidCredentials -> getString(R.string.wrong_credentials)
            is DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
            is DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
            else -> getString(R.string.auth_failed_generic)
        }

        showSnackbar(message)
    }
}