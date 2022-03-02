package com.unina.natourkt.presentation.new_route

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRoutePhotosBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment

class NewRoutePhotosFragment : BaseFragment<FragmentNewRoutePhotosBinding, NewRouteViewModel>() {

    private val viewModel: NewRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentNewRoutePhotosBinding.inflate(layoutInflater)

}