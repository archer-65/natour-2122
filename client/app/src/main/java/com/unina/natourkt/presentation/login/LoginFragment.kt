package com.unina.natourkt.presentation.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private lateinit var usernameField: TextInputLayout
    private lateinit var passwordField: TextInputLayout

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.linearlayoutSocial.applyInsetter {
            type(navigationBars = true) {
                padding()
            }
        }

        binding.imageLogin.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        loginButton = binding.buttonLogin
        registerButton = binding.buttonSignup

        usernameField = binding.textfieldUsername
        passwordField = binding.textfieldPassword

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For Social Login (required by Amplify)
        val activity = requireActivity()

        collectState()

        loginButton.setOnClickListener {
            loginViewModel.login(
                usernameField.editText?.text.toString(),
                passwordField.editText?.text.toString()
            )
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_login_to_navigation_registration)
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
                loginViewModel.uiState.collect { uiState ->
                    if (uiState.isUserLoggedIn) {
                        findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
                    }
                    if (uiState.isLoading) {
                        Toast.makeText(
                            this@LoginFragment.activity,
                            "Aspe amo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (uiState.errorMessage != null) {
                        Toast.makeText(
                            this@LoginFragment.activity,
                            "Aspe error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}