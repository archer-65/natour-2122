package com.unina.natourkt.core.presentation.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewbinding.ViewBinding

/**
 * This open class extending [Fragment] provides basic functionality
 * for Fragments which extends it
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    /* A way to get the MainViewModel from the activity, and not from the fragment. */
    /* val mainViewModel: MainViewModel by activityViewModels() */

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
        initRecycler()
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
     * This function initialize a [ConcatAdapter]
     */
    open fun initConcatAdapter(): ConcatAdapter {
        return ConcatAdapter()
    }

    /**
     * This function serves as a way to collect states from ViewModel
     */
    open fun collectState() {}
}