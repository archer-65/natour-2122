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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Basic settings for UI
     */
    fun setupUi() {
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
    fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationViewModel.uiRegistrationState.collect { uiState ->
                    if (uiState.isSignUpComplete) {
                        progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_navigation_registration_to_navigation_confirmation)
                    }
                    if (uiState.isLoading) {
                        progressBar.visibility = View.GONE
                    }
                    if (uiState.errorMessage != null) {
                        progressBar.visibility = View.GONE
                        Snackbar.make(
                            this@RegistrationFragment.requireView(),
                            uiState.errorMessage,
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
        registerButton.setOnClickListener {
            registrationViewModel.registration(
                usernameField.editText?.text.toString(),
                emailField.editText?.text.toString(),
                passwordField.editText?.text.toString()
            )
        }
    }
}