package com.unina.natourkt.presentation.registration

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
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
import com.unina.natourkt.databinding.FragmentRegistrationBinding
import com.unina.natourkt.presentation.base.BaseFragment
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

    // Buttons
    private lateinit var registerButton: Button

    // TextFields
    private lateinit var usernameField: TextInputLayout
    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var passwordConfirmField: TextInputLayout

    // ProgressBar
    private lateinit var progressBar: ProgressBar

    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
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
        binding.buttonSignup.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        binding.imageRegistration.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        registerButton = binding.buttonSignup

        usernameField = binding.textfieldUsername
        emailField = binding.textfieldEmail
        passwordField = binding.textfieldPassword
        passwordConfirmField = binding.textfieldConfirmPassword

        progressBar = binding.progressBar
    }

    /**
     * Start to collect LoginState, action based on Success/Loading/Error
     */
    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationViewModel.uiRegistrationState.collect { uiState ->
                    if (uiState.isSignUpComplete) {
                        // When the user signs up, then the progress bar disappears and
                        // we can navigate to Confirmation screen
                        progressBar.inVisible()
                        findNavController().navigate(R.id.navigation_registration_to_navigation_confirmation)
                    }
                    if (uiState.isLoading) {
                        // While loading display progress
                        progressBar.visible()
                    }
                    if (uiState.errorMessage != null) {
                        // When there's an error the progress bar disappears and
                        // a message is displayed
                        val message = when (uiState.errorMessage) {
                            DataState.CustomMessages.UsernameExists -> getString(R.string.username_exists)
                            DataState.CustomMessages.AliasExists -> getString(R.string.credentials_already_taken)
                            DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
                            DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
                            else -> getString(R.string.auth_failed_generic)
                        }
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
        registerButton.setOnClickListener {
            if (isFormValid()) {
                registrationViewModel.registration(
                    usernameField.editText?.text.toString(),
                    emailField.editText?.text.toString(),
                    passwordField.editText?.text.toString()
                )
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

        emailField.editText?.doAfterTextChanged {
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
        registerButton.isEnabled = usernameField.editText?.text!!.isNotBlank()
                && emailField.editText?.text!!.isNotBlank()
                && passwordField.editText?.text!!.isNotBlank()
                && passwordConfirmField.editText?.text!!.isNotBlank()
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        val isUsernameValid = isValidUsername()
        val isEmailValid = isEmailValid()
        val isPasswordValid = isValidPassword()

        return isUsernameValid && isPasswordValid && isEmailValid
    }

    private fun isValidUsername(): Boolean {

        val username = usernameField.editText?.text!!.trim().toString()

        return if (username.contains(" ")) {
            usernameField.error = "L'username non può contenere spazi."
            false
        } else {
            usernameField.error = null
            true
        }
    }

    private fun isEmailValid(): Boolean {

        val email = emailField.editText?.text!!.trim().toString()
        val match = Patterns.EMAIL_ADDRESS.matcher(email).matches()

        return if (!match) {
            emailField.error = "Email non valida."
            false
        } else {
            emailField.error = null
            true
        }
    }

    private fun isValidPassword(): Boolean {

        val password = passwordField.editText?.text!!.trim().toString()
        val confirmPassword = passwordConfirmField.editText?.text!!.trim().toString()

        return if (password.length < 7) {
            passwordField.error = "La password deve contenere almeno 7 caratteri."
            false
        } else if (password != confirmPassword) {
            passwordConfirmField.error = "La password non corrisponde."
            passwordField.error = null
            false
        } else {
            passwordField.error = null
            passwordConfirmField.error = null
            true
        }
    }
}