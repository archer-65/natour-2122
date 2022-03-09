package com.unina.natourkt.presentation.routes

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.common.scrollBehavior
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentRoutesBinding
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.RouteAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the routes screen
 * filled of paginated routes
 */
@AndroidEntryPoint
class RoutesFragment : BaseFragment<FragmentRoutesBinding, RoutesViewModel>(),
    RouteAdapter.OnItemClickListener {

    private val recyclerAdapter = RouteAdapter(this@RoutesFragment)

    private val viewModel: RoutesViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRoutesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun setListeners() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                recyclerAdapter.refresh()
            }

            newRouteFab.setOnClickListener {
                val extras = FragmentNavigatorExtras(newRouteFab to "transitionNewRouteFab")
                findNavController().navigate(
                    R.id.action_routes_to_new_route_flow,
                    null,
                    null,
                    extras
                )
            }

            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search_route -> {
                        findNavController().navigate(R.id.action_navigation_routes_to_navigation_search_route2)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerRoutes.apply {
                layoutManager = LinearLayoutManager(this@RoutesFragment.requireContext())
                adapter = initConcatAdapter()
                scrollBehavior(newRouteFab)
            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {
        val footerLoadStateAdapter = ItemLoadStateAdapter()
        val headerLoadStateAdapter = ItemLoadStateAdapter()

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading

            shimmerContainer.isVisible = loadState.source.refresh is LoadState.Loading
            recyclerRoutes.isVisible = loadState.source.refresh !is LoadState.Loading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(routesFlow) {
            recyclerAdapter.submitData(it)
        }
    }

    override fun onItemClick(route: RouteItemUiState) {
        val action = RoutesFragmentDirections.actionRoutesToRouteDetails(
            route.id,
            route.authorId
        )
        findNavController().navigate(action)
    }
}