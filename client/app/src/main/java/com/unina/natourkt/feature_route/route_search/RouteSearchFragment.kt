package com.unina.natourkt.feature_route.route_search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRouteSearchBinding
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.RouteAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.setTopMargin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

    override fun setListeners() = with(binding) {

        topAppBar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.filter_search -> {
                        findNavController().navigate(R.id.action_search_route_to_bottomsheet_filter)
                        true
                    }
                    else -> false
                }
            }
        }

        binding.search.onActionViewExpanded()

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.onEvent(RouteSearchEvent.EnteredQuery(it))
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
        val footerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)
        val headerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)

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

    override fun onItemClick(route: RouteItemUi) {
        val action = RouteSearchFragmentDirections.actionGlobalNavigationRouteDetails(
            route.id,
            route.authorId
        )
        findNavController().navigate(action)
    }

    override fun onSaveClick(route: RouteItemUi) {
    }
}