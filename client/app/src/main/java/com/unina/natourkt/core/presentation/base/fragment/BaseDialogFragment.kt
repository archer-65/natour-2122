package com.unina.natourkt.core.presentation.base.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unina.natourkt.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This open class extending [Fragment] provides basic functionality
 * for Fragments which extends it
 */
abstract class BaseDialogFragment<VB : ViewBinding, VM : ViewModel> : DialogFragment() {

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

    protected open var shouldSetCustomView = false

    protected abstract var baseTitle: Int
    protected abstract fun getDialogTitle(): Int

    protected abstract var baseMessage: Int
    protected abstract fun getDialogMessage(): Int

    protected var baseIcon: Int? = null
    protected open fun getDialogIcon(): Int? = baseIcon

    protected var basePositive: Int? = null
    protected open fun getDialogPositive(): Int? = basePositive

    protected var baseNegative: Int? = null
    protected open fun getDialogNegative(): Int? = baseNegative

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        init()
        val dialog = initDialog()
        dialog.setListeners()

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
        baseTitle = getDialogTitle()
        baseIcon = getDialogIcon()
        baseMessage = getDialogMessage()
        basePositive = getDialogPositive()
        baseNegative = getDialogNegative()
    }

    open fun initDialog(): AlertDialog {
        val baseDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(baseTitle)
            .setMessage(baseMessage)
            .setCancelable(false)

        if (shouldSetCustomView) baseDialog.setView(binding.root)

        baseDialog.initAddIcon()
        baseDialog.initAddButtons()

        return baseDialog.create()
    }

    open fun MaterialAlertDialogBuilder.initAddIcon() {
        baseIcon?.let { this.setIcon(it) }
    }

    open fun MaterialAlertDialogBuilder.initAddButtons() {
        basePositive?.let {
            this.setPositiveButton(it, null)
        }

        baseNegative?.let {
            this.setNegativeButton(it, null)
        }
    }

    open fun AlertDialog.setListeners() {
        val dialog = this
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                positiveAction()
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                negativeAction()
            }
        }
    }

    open fun positiveAction() {}

    open fun negativeAction() {}

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