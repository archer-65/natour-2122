package com.unina.natourkt.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.common.inVisible
import com.unina.natourkt.common.visible
import com.unina.natourkt.databinding.FragmentConfirmationBinding
import com.unina.natourkt.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * This Fragment represents the Confirmation after Sign Up Screen
 */
@AndroidEntryPoint
class ConfirmationFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!

    // Buttons
    private lateinit var confirmationButton: Button

    // TextFields
    private lateinit var codeField: TextInputLayout

    // Progress Bar
    private lateinit var progressBar: ProgressBar

    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
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
        binding.imageConfirmation.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        confirmationButton = binding.buttonConfirmation

        codeField = binding.textfieldConfirmCode

        progressBar = binding.progressBar
    }

    /**
     * Start to collect LoginState, action based on Success/Loading/Error
     */
    fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationViewModel.uiConfirmationState.collect { uiState ->
                    if (uiState.isConfirmationComplete) {
                        // When the user confirms sign up, then the progress bar disappears and
                        // we can navigate to Home screen
                        progressBar.inVisible()
                        val message = "Account confermato, effettua l'accesso!"
                        Snackbar.make(
                            this@ConfirmationFragment.requireView(),
                            message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_navigation_confirmation_to_navigation_login)
                    }
                    if (uiState.isLoading) {
                        // While loading display progress
                        progressBar.visible()
                    }
                    if (uiState.errorMessage != null) {
                        // When there's an error the progress bar disappears and
                        // a message is displayed
                        val message = when (uiState.errorMessage) {
                            DataState.CustomMessages.CodeDelivery -> getString(R.string.error_confirmation_code_deliver)
                            DataState.CustomMessages.CodeMismatch -> getString(R.string.wrong_confirmation_code)
                            DataState.CustomMessages.CodeExpired -> getString(R.string.expired_confirmation_code)
                            DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
                            else -> getString(R.string.auth_failed_generic)
                        }
                        progressBar.inVisible()
                        Snackbar.make(
                            this@ConfirmationFragment.requireView(),
                            message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    /**
     * Function to set listeners for views
     */
    fun setListeners() {
        confirmationButton.setOnClickListener {
            registrationViewModel.confirmation(codeField.editText?.text.toString())
        }
    }
}