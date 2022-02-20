package com.unina.natourkt.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
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

    // Buttons
    private lateinit var loginButton: Button
    private lateinit var googleButton: Button
    private lateinit var facebookButton: Button
    private lateinit var forgotPasswordButton: Button

    // TextFields
    private lateinit var usernameField: TextInputLayout
    private lateinit var passwordField: TextInputLayout

    // TextViews
    private lateinit var registerTextButton: TextView

    // ProgressBar
    private lateinit var progressBar: ProgressBar

    // ViewModel
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

        binding.buttonFacebook.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        binding.imageLogin.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        loginButton = binding.buttonLogin
        registerTextButton = binding.textviewSignup
        googleButton = binding.buttonGoogle
        facebookButton = binding.buttonFacebook

        forgotPasswordButton = binding.buttonForgotPassword

        usernameField = binding.textfieldUsername
        passwordField = binding.textfieldPassword

        progressBar = binding.progressBar
    }

    /**
     * Start to collect [LoginUiState], action based on Success/Loading/Error
     */
    private fun collectState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collect { uiState ->
                    // When the user is logged in
                    if (uiState.isUserLoggedIn) {
                        // The progress bar disappears
                        progressBar.inVisible()

                        // We navigate to the home screen
                        findNavController().navigate(R.id.navigation_login_to_navigation_home)
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
                            is DataState.CustomMessages.UserNotConfirmed -> getString(R.string.user_not_confirmed)
                            is DataState.CustomMessages.InvalidPassword -> getString(R.string.invalid_password)
                            is DataState.CustomMessages.InvalidCredentials -> "Username o password non corretti, riprova."
                            is DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
                            is DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
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
        loginButton.setOnClickListener {
            if (isFormValid()) {
                loginViewModel.login(
                    usernameField.editText?.text.toString(),
                    passwordField.editText?.text.toString(),
                )
            }
        }

        forgotPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_login_to_navigation_forgot_password)
        }

        registerTextButton.setOnClickListener {
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
    private fun setTextChangedListeners() {
        usernameField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }

        passwordField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }
    }

    /**
     * Validate form to enable button
     */
    private fun isFormValidForButton() {
        loginButton.isEnabled =
            usernameField.editText?.text!!.isNotBlank() && passwordField.editText?.text!!.isNotBlank()
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
    private fun isUsernameValid(): Boolean {

        val username = usernameField.editText?.text!!.trim().toString()

        return if (username.contains(" ")) {
            usernameField.error = "L'username non può contenere spazi."
            false
        } else {
            usernameField.error = null
            true
        }
    }

    /**
     * Check if the password is valid and manage TextField errors
     */
    private fun isPasswordValid(): Boolean {

        val password = passwordField.editText?.text!!.trim().toString()

        return if (password.length < PASSWORD_LENGTH) {
            passwordField.error = "La password deve contenere almeno 7 caratteri."
            false
        } else {
            passwordField.error = null
            true
        }
    }
}