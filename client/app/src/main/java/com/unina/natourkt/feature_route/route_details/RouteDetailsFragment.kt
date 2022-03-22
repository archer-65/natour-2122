package com.unina.natourkt.feature_route.route_details

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRouteDetailsBinding
import com.unina.natourkt.core.presentation.adapter.ViewPagerAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.feature_route.route_details.info.RouteDetailsInfoFragment
import com.unina.natourkt.feature_route.route_details.map.RouteDetailsMapFragment
import com.unina.natourkt.feature_route.route_details.tag.RouteDetailsTagFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteDetailsFragment : BaseFragment<FragmentRouteDetailsBinding, RouteDetailsViewModel>() {

    private val viewModel: RouteDetailsViewModel by hiltNavGraphViewModels(R.id.navigation_route_details_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRouteDetailsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
    }

    override fun setupUi() {
        binding.topAppBar.setTopMargin()
    }

    override fun collectState() {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                it.menu?.let { setupToolbar(it) }
            }
        }
    }

    private fun setupToolbar(menu: Int) = with(binding) {
        topAppBar.apply {
            this.menu.clear()
            inflateMenu(menu)

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                onMenuClick(it)
            }

        }
    }

    private fun setupViewPager() = with(binding) {

        val fragmentList = arrayListOf(
            RouteDetailsInfoFragment(),
            RouteDetailsMapFragment(),
            RouteDetailsTagFragment()
        )

        val adapter = ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        viewPager.isUserInputEnabled = false
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "INFO"
                1 -> tab.text = "MAPPA"
                2 -> tab.text = "TAG"
            }
        }.attach()
    }

    private fun onMenuClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.report_route -> {
                val action =
                    RouteDetailsFragmentDirections.actionNavigationRouteDetailsToNavigationDialogReportRoute(
                        viewModel.routeId
                    )
                findNavController().navigate(action)
            }
            R.id.save_route -> {
                val action = RouteDetailsFragmentDirections.actionGlobalSaveIntoCompilationDialog(
                    viewModel.routeId,
                    viewModel.uiState.value.loggedUser!!.id,
                )
                findNavController().navigate(action)
            }
            R.id.update_route -> {
                val action =
                    RouteDetailsFragmentDirections.actionNavigationRouteDetailsToUpdateRouteFullDialog2(
                        viewModel.uiState.value.route!!
                    )
                findNavController().navigate(action)
            }
            R.id.delete_route -> {
                val action = RouteDetailsFragmentDirections.actionGlobalDeleteRouteDialog3(
                    viewModel.routeId
                )
                findNavController().navigate(action)
            }
        }
        return true
    }
}