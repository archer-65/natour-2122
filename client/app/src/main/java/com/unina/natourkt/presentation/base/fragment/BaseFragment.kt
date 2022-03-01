package com.unina.natourkt.presentation.base.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
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

    /* A way to get the MainViewModel from the activity, and not from the fragment. */
    val mainViewModel: MainViewModel by activityViewModels()

    /**
     * This function setups every Ui related option, for example apply margins with [Insetter]
     */
    open fun setupUi() {}

    /**
     * This function sets any kind of listener
     */
    open fun setListeners() {}

    /**
     * This function initialize any recycler view
     */
    open fun initRecycler() {}

    /**
     * This function initialize a ConcatAdapter
     */
    open fun initConcatAdapter(): ConcatAdapter {
        return ConcatAdapter()
    }

    /**
     * This function serves as a way to collect states from ViewModel
     */
    open fun collectState() {}

    /**
     * It launches a coroutine on the view's lifecycle scope, and repeats the coroutine on the view's
     * lifecycle until the view's lifecycle is in the STARTED state
     */
    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                execute()
            }
        }
    }

    /**
     * Display a snackbar with a given message
     */
    fun showSnackbar(message: String) {
        Snackbar.make(this.requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Get the right message and show it to the user
     */
    fun manageMessage(customMessage: DataState.CustomMessage) {
        val message = when (customMessage) {
            is DataState.CustomMessage.UserNotFound -> getString(R.string.user_not_found)
            is DataState.CustomMessage.UserNotConfirmed -> getString(R.string.user_not_confirmed)
            is DataState.CustomMessage.InvalidPassword -> getString(R.string.invalid_password)
            is DataState.CustomMessage.InvalidCredentials -> getString(R.string.wrong_credentials)
            is DataState.CustomMessage.UsernameExists -> getString(R.string.username_exists)
            is DataState.CustomMessage.AliasExists -> getString(R.string.credentials_already_taken)
            is DataState.CustomMessage.InvalidParameter -> getString(R.string.incorrect_parameters)
            is DataState.CustomMessage.CodeDelivery -> getString(R.string.error_confirmation_code_deliver)
            is DataState.CustomMessage.CodeMismatch -> getString(R.string.wrong_confirmation_code)
            is DataState.CustomMessage.CodeExpired -> getString(R.string.expired_confirmation_code)
            is DataState.CustomMessage.AuthGeneric -> getString(R.string.auth_failed_exception)
            else -> getString(R.string.auth_failed_generic)
        }

        showSnackbar(message)
    }
}