package com.unina.natourkt.feature_route.routes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.CompilationDialogAdapter
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.RouteAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.CompilationDialogItemUi
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.scrollBehavior
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.databinding.FragmentRoutesBinding
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
                    R.id.action_routes_to_create_route_flow,
                    null,
                    null,
                    extras
                )
            }

            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search_route -> {
                        findNavController().navigate(R.id.action_routes_to_route_search)
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

        collectLatestOnLifecycleScope(eventFlow) { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Snackbar.make(
                        binding.newRouteFab,
                        event.uiText.asString(requireContext()),
                        Snackbar.LENGTH_SHORT
                    ).setAnchorView(binding.newRouteFab).show()
                }
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        requireContext(),
                        event.uiText.asString(requireContext()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }

    override fun onItemClick(route: RouteItemUi) {
        val action = RoutesFragmentDirections.actionRoutesToRouteDetails(
            route.id,
            route.authorId
        )
        findNavController().navigate(action)
    }

    override fun onSaveClick(route: RouteItemUi) {
        val action = RoutesFragmentDirections.actionNavigationRoutesToSaveIntoCompilationDialog(
            route.id,
            viewModel.uiState.value.loggedUser!!.id
        )
        findNavController().navigate(action)
    }
}