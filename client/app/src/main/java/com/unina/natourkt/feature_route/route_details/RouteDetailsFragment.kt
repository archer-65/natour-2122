package com.unina.natourkt.feature_route.route_details

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRouteDetailsBinding
import com.unina.natourkt.core.presentation.adapter.ViewPagerAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.feature_route.route_details.info.RouteDetailsInfoFragment
import com.unina.natourkt.feature_route.route_details.map.RouteDetailsMapFragment
import com.unina.natourkt.feature_route.route_details.tag.RouteDetailsTagFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RouteDetailsFragment : BaseFragment<FragmentRouteDetailsBinding, RouteDetailsViewModel>() {

    private val viewModel: RouteDetailsViewModel by hiltNavGraphViewModels(R.id.navigation_route_details_flow)
    private val args: RouteDetailsFragmentArgs by navArgs()

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
                when (it.itemId) {
                    R.id.report_route -> {
                        val action =
                            RouteDetailsFragmentDirections.actionNavigationRouteDetailsToNavigationDialogReportRoute(
                                args.routeId
                            )
                        findNavController().navigate(action)
                        true
                    }
                    R.id.delete_route -> {
                        true
                    }
                    R.id.save_route -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
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
}