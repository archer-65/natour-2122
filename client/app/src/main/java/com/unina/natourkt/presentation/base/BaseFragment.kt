package com.unina.natourkt.presentation.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.unina.natourkt.presentation.MainViewModel

open class BaseFragment : Fragment() {

    val mainViewModel: MainViewModel by activityViewModels()
}