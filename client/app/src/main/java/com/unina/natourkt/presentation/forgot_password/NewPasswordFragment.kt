package com.unina.natourkt.presentation.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.inVisible
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentNewPasswordBinding
import com.unina.natourkt.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewPasswordFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!

    // Buttons
    private lateinit var resetPasswordButton: Button

    // TextFields
    private lateinit var confirmCodeField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var passwordConfirmField: TextInputLayout

    // ProgressBar
    private lateinit var progressBar: ProgressBar

    private val newPasswordViewModel: NewPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
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
        binding.buttonResetPassword.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        binding.imageForgotPassword.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        resetPasswordButton = binding.buttonResetPassword

        confirmCodeField = binding.textfieldConfirmCodeReset
        passwordField = binding.textfieldPassword
        passwordConfirmField = binding.textfieldConfirmPassword

        progressBar = binding.progressBar
    }

    fun collectState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newPasswordViewModel.uiState.collect { uiState ->
                    if (uiState.isPasswordReset) {
                        // When the password is resetted, navigate to login
                        progressBar.inVisible()
                        val message =
                            "La password è stata aggiornata con successo! Effettua l'accesso."
                        showSnackbar(message)
                        findNavController().navigate(R.id.navigation_new_password_to_navigation_login)
                    }
                    if (uiState.isLoading) {
                        // While loading display progress
                        progressBar.visible()
                    }
                    if (uiState.errorMessage != null) {
                        // When there's an error the progress bar disappears and
                        // a message is displayed
                        val message = when (uiState.errorMessage) {
                            is DataState.CustomMessages.CodeDelivery -> getString(R.string.error_confirmation_code_deliver)
                            is DataState.CustomMessages.CodeMismatch -> getString(R.string.wrong_confirmation_code)
                            is DataState.CustomMessages.CodeExpired -> getString(R.string.expired_confirmation_code)
                            is DataState.CustomMessages.InvalidPassword -> getString(R.string.invalid_password)
                            is DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
                            is DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
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
        resetPasswordButton.setOnClickListener {
            newPasswordViewModel.resetConfirm(
                passwordField.editText?.text.toString(),
                confirmCodeField.editText?.text.toString()
            )
        }
    }
}