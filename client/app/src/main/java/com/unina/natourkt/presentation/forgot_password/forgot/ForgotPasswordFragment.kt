package com.unina.natourkt.presentation.forgot_password.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentForgotPasswordBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

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
    private val forgotPasswordViewModel: ForgotPasswordViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
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

    override fun setupUi() {
        with(binding) {
            forgotPasswordImage.applyInsetter {
                type(statusBars = true) {
                    margin()
                }
            }
        }
    }

    override fun setListeners() {
        with(binding) {
            sendCodeButton.setOnClickListener {
                if (isFormValid()) {
                    forgotPasswordViewModel.resetRequest(usernameTextField.editText?.text.toString())
                }
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() {
        with(binding) {
            usernameTextField.editText?.doAfterTextChanged {
                val username = it.toString().trim()
                forgotPasswordViewModel.setUsername(username)
            }
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean {
        with(binding) {
            val isUsernameValid =
                forgotPasswordViewModel.formState.value.isUsernameValid.also { valid ->
                    val error = if (!valid) getString(R.string.username_check) else null
                    usernameTextField.error = error
                }

            return isUsernameValid
        }
    }

    override fun collectState() = with(binding) {
        with(forgotPasswordViewModel) {
            launchOnLifecycleScope {
                uiState.collect {
                    // When the code is sent
                    if (it.isCodeSent) {
                        // Show a message
                        val message = getString(R.string.code_resent_password)
                        showSnackbar(message)

                        // We navigate to new password insertion screen
                        findNavController().navigate(R.id.action_forgot_password_to_new_password)
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
                    sendCodeButton.isEnabled = it.username.isNotBlank()
                }
            }
        }
    }
}