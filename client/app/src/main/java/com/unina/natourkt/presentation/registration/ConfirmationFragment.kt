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
import com.unina.natourkt.databinding.FragmentConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ConfirmationFragment : Fragment() {

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
                        progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_navigation_confirmation_to_navigation_home)
                    }
                    if (uiState.isLoading) {
                        progressBar.visibility = View.VISIBLE
                    }
                    if (uiState.errorMessage != null) {
                        progressBar.visibility = View.GONE
                        Snackbar.make(
                            this@ConfirmationFragment.requireView(),
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
        confirmationButton.setOnClickListener {
            registrationViewModel.confirmation(codeField.editText?.text.toString())
        }
    }
}