package com.unina.natourkt.presentation.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
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
import com.google.android.material.snackbar.Snackbar
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

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Buttons
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    // TextFields
    private lateinit var usernameField: TextInputLayout
    private lateinit var passwordField: TextInputLayout

    // ProgressBar
    private lateinit var progressBar: ProgressBar

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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

        progressBar = binding.progressBar
    }

    /**
     * Start to collect LoginState, action based on Success/Loading/Error
     */
    fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collect { uiState ->
                    if (uiState.isUserLoggedIn) {
                        progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
                    }
                    if (uiState.isLoading) {
                        progressBar.visibility = View.VISIBLE
                    }
                    if (uiState.errorMessage != null) {
                        progressBar.visibility = View.GONE
                        Snackbar.make(
                            this@LoginFragment.requireView(),
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
}