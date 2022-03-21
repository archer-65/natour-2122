package com.unina.natourkt.feature_auth.registration.confirmation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentConfirmationBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.feature_auth.registration.signup.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the Confirmation after Sign Up Screen
 */
@AndroidEntryPoint
class ConfirmationFragment : BaseFragment<FragmentConfirmationBinding, ConfirmationViewModel>() {

    private val viewModel: ConfirmationViewModel by hiltNavGraphViewModels(R.id.navigation_auth_flow)
    private val args: ConfirmationFragmentArgs by navArgs()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentConfirmationBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setTextChangedListeners()
    }

    override fun setupUi() = with(binding) {
        confirmationImage.setTopMargin()
    }

    override fun setListeners() = with(binding) {
        with(viewModel) {
            confirmationButton.setOnClickListener {
                onEvent(ConfirmationEvent.Confirm(args.userConfirm))
            }

            resendCodeButton.setOnClickListener {
                onEvent(ConfirmationEvent.Resend(args.userConfirm))
            }
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            confirmCodeTextField.updateText {
                onEvent(ConfirmationEvent.EnteredCode(it))
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                // When the user is confirmed a message is shown and we navigate to the login screen
                if (it.isConfirmationComplete) {
                    findNavController().navigate(R.id.action_confirmation_to_login)
                }

                progressBar.isVisible = it.isLoading
            }

            collectOnLifecycleScope(formState) {
                confirmationButton.isEnabled = it.isButtonEnabled
                confirmCodeTextField.error = it.code.error?.asString(requireContext())
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        Snackbar.make(
                            requireView(),
                            event.uiText.asString(requireContext()),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
                }
            }
        }
    }
}