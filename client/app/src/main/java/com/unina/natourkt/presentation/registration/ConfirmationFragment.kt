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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentConfirmationBinding
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ConfirmationFragment : Fragment() {

    private var _binding: FragmentConfirmationBinding? = null

    private val binding get() = _binding!!

    private lateinit var confirmationButton: Button

    private lateinit var codeField: TextInputLayout

    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.imageConfirmation.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        confirmationButton = binding.buttonConfirmation

        codeField = binding.textfieldConfirmCode

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()

        confirmationButton.setOnClickListener {
            registrationViewModel.confirmation(codeField.editText?.text.toString())
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
                registrationViewModel.uiConfirmationState.collect { uiState ->
                    if (uiState.isConfirmationComplete) {
                        findNavController().navigate(R.id.action_navigation_confirmation_to_navigation_home)
                    }
                    if (uiState.isLoading) {
                        Toast.makeText(
                            this@ConfirmationFragment.activity,
                            "Aspe amo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (uiState.errorMessage != null) {
                        Toast.makeText(
                            this@ConfirmationFragment.activity,
                            uiState.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}