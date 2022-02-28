package com.unina.natourkt.presentation.base.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.presentation.main.MainViewModel
import kotlinx.coroutines.launch

/**
 * This open class extending [Fragment] provides basic functionality
 * for Fragments which extends it:
 * - [MainViewModel] is the basic ViewModel, present across all fragments to avoid
 * continuous request for logged-in users
 * - [Snackbar] showing function
 */
open class BaseFragment : Fragment() {

    val mainViewModel: MainViewModel by activityViewModels()

    fun showSnackbar(message: String) {
        Snackbar.make(this.requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    fun manageMessage(customMessage: DataState.CustomMessages) {
        // Get the right message
        val message = when (customMessage) {
            is DataState.CustomMessages.UserNotFound -> getString(R.string.user_not_found)
            is DataState.CustomMessages.UserNotConfirmed -> getString(R.string.user_not_confirmed)
            is DataState.CustomMessages.InvalidPassword -> getString(R.string.invalid_password)
            is DataState.CustomMessages.InvalidCredentials -> getString(R.string.wrong_credentials)
            is DataState.CustomMessages.UsernameExists -> getString(R.string.username_exists)
            is DataState.CustomMessages.AliasExists -> getString(R.string.credentials_already_taken)
            is DataState.CustomMessages.InvalidParameter -> getString(R.string.incorrect_parameters)
            is DataState.CustomMessages.CodeDelivery -> getString(R.string.error_confirmation_code_deliver)
            is DataState.CustomMessages.CodeMismatch -> getString(R.string.wrong_confirmation_code)
            is DataState.CustomMessages.CodeExpired -> getString(R.string.expired_confirmation_code)
            is DataState.CustomMessages.AuthGeneric -> getString(R.string.auth_failed_exception)
            else -> getString(R.string.auth_failed_generic)
        }

        showSnackbar(message)
    }

    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                execute()
            }
        }
    }
}