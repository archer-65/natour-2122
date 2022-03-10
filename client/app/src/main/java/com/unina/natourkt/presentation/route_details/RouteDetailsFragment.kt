package com.unina.natourkt.presentation.route_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import com.google.android.material.tabs.TabLayoutMediator
import com.unina.natourkt.R
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentRouteDetailsBinding
import com.unina.natourkt.presentation.base.adapter.ViewPagerAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.UserUiState
import com.unina.natourkt.presentation.post_details.PostDetailsFragmentArgs
import com.unina.natourkt.presentation.route_details.info.RouteDetailsInfoFragment
import com.unina.natourkt.presentation.route_details.map.RouteDetailsMapFragment
import com.unina.natourkt.presentation.route_details.tag.RouteDetailsTagFragment
import dagger.hilt.android.AndroidEntryPoint

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

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                setupToolbar(it.loggedUser)
            }
        }
    }

    private fun setupToolbar(loggedUser: UserUiState?) = with(binding) {
        topAppBar.apply {
            menu.clear()

            if (loggedUser?.id == args.authorId) {
                inflateMenu(R.menu.top_bar_owner_route_menu)
            } else {
                inflateMenu(R.menu.top_bar_viewer_route_menu)
            }

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.report_route -> {
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

        val fragmentList = arrayListOf<Fragment>(
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