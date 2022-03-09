package com.unina.natourkt.presentation.route_search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.common.scrollBehavior
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentRouteSearchBinding
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.RouteAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import com.unina.natourkt.presentation.routes.RoutesFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RouteSearchFragment : BaseFragment<FragmentRouteSearchBinding, RouteSearchViewModel>(),
    RouteAdapter.OnItemClickListener {

    private val recyclerAdapter = RouteAdapter(this@RouteSearchFragment)

    private val viewModel: RouteSearchViewModel by hiltNavGraphViewModels(R.id.navigation_search_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRouteSearchBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setupUi() {
        binding.topAppBar.setTopMargin()
    }

    override fun setListeners() {

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter_search -> {
                    findNavController().navigate(R.id.action_navigation_search_route_to_bottomSheetFilterFragment)
                    true
                }
                else -> false
            }
        }

        binding.search.onActionViewExpanded()

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.recyclerRoutes.scrollToPosition(0)
                    clearRecycler()
                    viewModel.setQuery(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun initRecycler() {
        with(binding) {
            recyclerRoutes.apply {
                layoutManager = LinearLayoutManager(this@RouteSearchFragment.requireContext())
                adapter = initConcatAdapter()
            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {
        val footerLoadStateAdapter = ItemLoadStateAdapter()
        val headerLoadStateAdapter = ItemLoadStateAdapter()

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            recyclerRoutes.isVisible = loadState.source.refresh is LoadState.NotLoading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(routeResults) {
            recyclerAdapter.submitData(it)
        }
    }

    private fun clearRecycler() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            recyclerAdapter.submitData(PagingData.empty())
        }
    }

    override fun onItemClick(route: RouteItemUiState) {
        val action = RouteSearchFragmentDirections.actionGlobalNavigationRouteDetails(
            route.id,
            route.authorId
        )
        findNavController().navigate(action)
    }
}