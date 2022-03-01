package com.unina.natourkt.presentation.forgot_password.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentResetPasswordBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val resetPasswordViewModel: ResetPasswordViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
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
     * Start to collect [ResetPasswordUiState], action based on Success/Loading/Error
     */
    override fun collectState() {
        with(binding) {
            with(resetPasswordViewModel) {
                launchOnLifecycleScope {
                    uiState.collect {
                        // When the password is reset
                        if (it.isPasswordReset) {
                            val message = getString(R.string.password_reset_success)
                            showSnackbar(message)

                            // We navigate to login screen
                            findNavController().navigate(R.id.action_new_password_to_login)
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
                        passwordResetButton.isEnabled =
                            it.code.isNotBlank() && it.password.isNotBlank() && it.confirmPassword.isNotBlank()
                    }
                }
            }
        }
    }

    /**
     * Basic settings for UI
     */
    override fun setupUi() {
        with(binding) {
            passwordResetButton.applyInsetter {
                type(navigationBars = true) {
                    margin()
                }
            }

            forgotPasswordImage.applyInsetter {
                type(statusBars = true) {
                    margin()
                }
            }
        }
    }

    /**
     * Function to set listeners for views
     */
    override fun setListeners() {
        with(binding) {
            passwordResetButton.setOnClickListener {
                if (isFormValid()) {
                    resetPasswordViewModel.resetConfirm(
                        passwordTextField.editText?.text.toString(),
                        confirmCodeTextField.editText?.text.toString()
                    )
                }
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() = with(binding) {
        with(resetPasswordViewModel) {
            confirmCodeTextField.editText?.doAfterTextChanged {
                val code = it.toString().trim()
                setCode(code)
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
        val isCodeValid =
            resetPasswordViewModel.formState.value.isCodeValid.also { valid ->
                val error = if (!valid) getString(R.string.code_check) else null
                confirmCodeTextField.error = error
            }

        val isPasswordValid =
            resetPasswordViewModel.formState.value.isPasswordValid.also { valid ->
                val error = if (!valid) getString(R.string.password_length) else null
                passwordTextField.error = error
            }

        val isConfirmPasswordValid =
            resetPasswordViewModel.formState.value.isConfirmPasswordValid.also { valid ->
                val error = if (!valid) getString(R.string.passwords_matching) else null
                confirmPasswordTextField.error = error
            }

        return isCodeValid && isPasswordValid && isConfirmPasswordValid
    }
}