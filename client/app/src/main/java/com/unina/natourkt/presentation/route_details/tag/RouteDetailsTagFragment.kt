package com.unina.natourkt.presentation.route_details.tag

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRouteDetailsTagBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.route_details.RouteDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteDetailsTagFragment :
    BaseFragment<FragmentRouteDetailsTagBinding, RouteDetailsViewModel>() {

    private val viewModel: RouteDetailsViewModel by hiltNavGraphViewModels(R.id.navigation_route_details_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRouteDetailsTagBinding.inflate(layoutInflater)
}