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
import com.unina.natourkt.databinding.FragmentForgotPasswordBinding
import com.unina.natourkt.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

    // Buttons
    private lateinit var sendCodeButton: Button

    // TextFields
    private lateinit var usernameField: TextInputLayout

    // ProgressBar
    private lateinit var progressBar: ProgressBar

    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
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

    fun setupUi() {
        binding.imageForgotPassword.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        sendCodeButton = binding.buttonSendCode

        usernameField = binding.textfieldUsername

        progressBar = binding.progressBar
    }

    fun collectState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forgotPasswordViewModel.uiState.collect { uiState ->
                    if (uiState.isCodeSent) {
                        // When the code for reset is sent, then the progress bar disappears and
                        // we can navigate to new password insertion screen
                        progressBar.inVisible()
                        val message = "Il codice per il reset della password è stato inviato!"
                        showSnackbar(message)
                        findNavController().navigate(R.id.navigation_forgot_password_to_navigation_new_password)
                    }
                    if (uiState.isLoading) {
                        // While loading display progress
                        progressBar.visible()
                    }
                    if (uiState.errorMessage != null) {
                        val message = when (uiState.errorMessage) {
                            is DataState.CustomMessages.UserNotFound -> getString(R.string.user_not_found)
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

    fun setListeners() {
        sendCodeButton.setOnClickListener {
            forgotPasswordViewModel.resetRequest(usernameField.editText?.text.toString())
        }
    }
}