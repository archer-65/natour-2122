package com.unina.natourkt.presentation.base.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.presentation.main.MainViewModel

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
}