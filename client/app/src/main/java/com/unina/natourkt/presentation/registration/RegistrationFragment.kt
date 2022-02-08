package com.unina.natourkt.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var registerButton: Button

    private lateinit var usernameField: TextInputLayout
    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var passwordConfirmField: TextInputLayout

    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()

        registerButton.setOnClickListener {
            registrationViewModel.registration(
                usernameField.editText?.text.toString(),
                emailField.editText?.text.toString(),
                passwordField.editText?.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Start to collect LoginState, action based on Success/Loading/Error
     */
    fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                registrationViewModel.uiRegistrationState.collect { uiState ->
                    if (uiState.isSignUpComplete) {
                        findNavController().navigate(R.id.action_navigation_registration_to_navigation_confirmation)
                    }
                    if (uiState.isLoading) {
                        Toast.makeText(
                            this@RegistrationFragment.activity,
                            "Aspe amo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (uiState.errorMessage != null) {
                        Toast.makeText(
                            this@RegistrationFragment.activity,
                            "Aspe error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}