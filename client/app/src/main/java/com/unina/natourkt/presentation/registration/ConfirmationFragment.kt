package com.unina.natourkt.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentConfirmationBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

/**
 * This Fragment represents the Confirmation after Sign Up Screen
 */
@AndroidEntryPoint
class ConfirmationFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
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
     * Start to collect LoginState, action based on Success/Loading/Error
     */
    override fun collectState() = with(binding) {
        with(registrationViewModel) {
            launchOnLifecycleScope {
                uiConfirmationState.collect {
                    // When the user is confirmed
                    if (it.isConfirmationComplete) {
                        // Message is shown
                        val message = getString(R.string.confirmed_account)
                        showSnackbar(message)

                        // We navigate to the login screen
                        findNavController().navigate(R.id.action_confirmation_to_login)
                    }

                    // When the code is resent
                    if (it.isCodeResent) {
                        // Message is shown
                        val message = getString(R.string.code_resent)
                        showSnackbar(message)
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
                uiConfirmationFormState.collectLatest {
                    // Bind the button visibility
                    confirmationButton.isEnabled = it.code.isNotBlank()
                }
            }
        }
    }


    /**
     * Basic settings for UI
     */
    override fun setupUi() {
        with(binding) {
            confirmationImage.applyInsetter {
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
        with(registrationViewModel) {
            confirmationButton.setOnClickListener {
                if (isFormValid()) {
                    confirmation(confirmCodeTextField.editText?.text.toString())
                }
            }

            resendCodeButton.setOnClickListener {
                resendCode()
            }
        }
    }

    /**
     * Function to set TextListeners
     */
    private fun setTextChangedListeners() = with(binding) {
        confirmCodeTextField.editText?.doAfterTextChanged {
            val code = it.toString().trim()
            registrationViewModel.setCode(code)
        }
    }

    /**
     * Form validation based on other functions
     */
    private fun isFormValid(): Boolean = with(binding) {
        val isCodeValid =
            registrationViewModel.uiConfirmationFormState.value.isCodeValid.also { valid ->
                val error = if (!valid) getString(R.string.code_check) else null
                confirmCodeTextField.error = error
            }

        return isCodeValid
    }
}