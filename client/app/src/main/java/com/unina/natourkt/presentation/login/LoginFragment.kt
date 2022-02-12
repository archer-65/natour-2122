package com.unina.natourkt.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.common.Constants.FACEBOOK
import com.unina.natourkt.common.Constants.GOOGLE
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.inVisible
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentLoginBinding
import com.unina.natourkt.presentation.base.BaseFragment
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

    // TextFields
    private lateinit var usernameField: TextInputLayout
    private lateinit var passwordField: TextInputLayout

    // TextViews
    private lateinit var forgotPasswordText: TextView
    private lateinit var registerTextButton: TextView

    // ProgressBar
    private lateinit var progressBar: ProgressBar

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Basic settings for UI
     */
    fun setupUi() {

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

        forgotPasswordText = binding.textviewForgotPassword

        usernameField = binding.textfieldUsername
        passwordField = binding.textfieldPassword

        progressBar = binding.progressBar
    }

    /**
     * Start to collect LoginState, action based on Success/Loading/Error
     */
    fun collectState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collect { uiState ->
                    if (uiState.isUserLoggedIn) {
                        // When the user becomes logged in, then the progress bar disappears and
                        // we can navigate to Home screen
                        progressBar.inVisible()
                        findNavController().navigate(R.id.navigation_login_to_navigation_home)
                    }
                    if (uiState.isLoading) {
                        // While loading display progress
                        progressBar.visible()
                    }
                    if (uiState.errorMessage != null) {
                        val message = when (uiState.errorMessage) {
                            is DataState.CustomMessages.UserNotFound -> getString(R.string.user_not_found)
                            is DataState.CustomMessages.UserNotConfirmed -> getString(R.string.user_not_confirmed)
                            is DataState.CustomMessages.InvalidPassword -> getString(R.string.invalid_password)
                            is DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
                            DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
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
    fun setListeners() {
        loginButton.setOnClickListener {
            loginViewModel.login(
                usernameField.editText?.text.toString(),
                passwordField.editText?.text.toString(),
            )
        }

        forgotPasswordText.setOnClickListener {
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
}