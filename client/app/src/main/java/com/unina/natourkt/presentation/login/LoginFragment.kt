package com.unina.natourkt.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.common.Constants.FACEBOOK
import com.unina.natourkt.common.Constants.GOOGLE
import com.unina.natourkt.databinding.FragmentLoginBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

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
    private val loginViewModel: LoginViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

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
     * Basic settings for UI
     */
    override fun setupUi() {
        with(binding) {
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
    }

    /**
     * Function to set listeners for views
     */
    override fun setListeners() = with(binding) {
        with(loginViewModel) {
            loginButton.setOnClickListener {
                if (isFormValid()) {
                    login(
                        usernameTextField.editText?.text.toString(),
                        passwordTextField.editText?.text.toString(),
                    )
                }
            }

            forgotPasswordButton.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_forgot_password)
            }

            signUpTextView.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_registration)
            }

            googleButton.setOnClickListener {
                login(GOOGLE)
            }

            facebookButton.setOnClickListener {
                login(FACEBOOK)
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() = with(binding) {
        with(loginViewModel) {
            usernameTextField.editText?.doAfterTextChanged {
                val username = it.toString().trim()
                setUsername(username)
            }

            passwordTextField.editText?.doAfterTextChanged {
                val password = it.toString().trim()
                setPassword(password)
            }
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean = with(binding) {
        val isUsernameValid = loginViewModel.formState.value.isUsernameValid.also { valid ->
            val error = if (!valid) getString(R.string.username_check) else null
            usernameTextField.error = error
        }
        val isPasswordValid = loginViewModel.formState.value.isPasswordValid.also { valid ->
            val error = if (!valid) getString(R.string.password_length) else null
            passwordTextField.error = error
        }

        return isUsernameValid && isPasswordValid
    }

    override fun collectState() = with(binding) {
        with(loginViewModel) {
            launchOnLifecycleScope {
                uiState.collect {
                    // When the user is logged in
                    if (it.isUserLoggedIn) {
                        // We navigate to the home screen
                        findNavController().navigate(R.id.navigation_login_to_navigation_home)
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
                formState.collectLatest {
                    // Bind the button visibility
                    loginButton.isEnabled = it.username.isNotBlank() && it.password.isNotBlank()
                }
            }
        }
    }
}