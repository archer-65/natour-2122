package com.unina.natourkt.core.presentation.base.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.FEATURE_NO_TITLE
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This open class extending [Fragment] provides basic functionality
 * for Fragments which extends it
 */
abstract class BaseFullDialogFragment<VB : ViewBinding, VM : ViewModel> : DialogFragment() {

    /**
     * This property serves as ViewModel generalization
     */
    protected lateinit var baseViewModel: VM
    protected abstract fun getVM(): VM

    /**
     * This property is valid only until onDestroyView is called
     */
    protected var _binding: VB? = null
    protected val binding get() = _binding!!
    protected abstract fun getViewBinding(): VB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_NaTour_FullScreenDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(FEATURE_NO_TITLE)
        return dialog
    }

    /**
     * Overrides `onCreateView` only to return binding's root view, initialized in [init]
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        return binding.root
    }

    /**
     * Overrides `onViewCreated`, here all the useful basic functions are called
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        setTextChangedListeners()
        collectState()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    /**
     * [_binding] is set to null. This because Fragments could outlive their views, so we clean up
     * every reference to the [ViewBinding] class
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * It instantiate a binding object for the view, and initializes the view model
     */
    open fun init() {
        _binding = getViewBinding()
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
     * This function serves as a way to collect states from ViewModel
     */
    open fun collectState() {}
}