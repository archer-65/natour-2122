package com.unina.natourkt.presentation.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.common.DataState
import com.unina.natourkt.presentation.main.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This open class extending [Fragment] provides basic functionality
 * for Fragments which extends it:
 * - [MainViewModel] is the basic ViewModel, present across all fragments to avoid
 * continuous request for logged-in users
 * - [Snackbar] showing function
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    /* A way to get the MainViewModel from the activity, and not from the fragment. */
    val mainViewModel: MainViewModel by activityViewModels()

    protected lateinit var baseViewModel: VM
    protected abstract fun getVM(): VM

    protected lateinit var binding: VB
    protected abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        collectState()
    }

    private fun init() {
        binding = getViewBinding()
        baseViewModel = getVM()
    }

    /**
     * This function setups every View
     */
    open fun setupUi() {}

    /**
     * This function sets any kind of listener
     */
    open fun setListeners() {}

    /**
     * This function sets any kind of text listener
     */
    open fun setTextChangedListeners() {}

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
    fun <T> collectLatestOnLifecycleScope(flow: Flow<T>, execute: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(execute)
            }
        }
    }

    /**
     * It launches a coroutine on the view's lifecycle scope, and repeats the coroutine on the view's
     * lifecycle until the view's lifecycle is in the STARTED state
     */
    fun <T> collectOnLifecycleScope(flow: Flow<T>, execute: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect(execute)
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