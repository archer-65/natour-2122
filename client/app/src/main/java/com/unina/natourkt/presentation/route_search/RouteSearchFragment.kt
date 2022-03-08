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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RouteSearchFragment : BaseFragment<FragmentRouteSearchBinding, RouteSearchViewModel>() {

    private val recyclerAdapter = RouteAdapter()

    private val viewModel: RouteSearchViewModel by viewModels()

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

        binding.search.onActionViewExpanded()

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.recyclerRoutes.scrollToPosition(0)
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

            shimmerContainer.isVisible = loadState.source.refresh is LoadState.Loading
            recyclerRoutes.isVisible = loadState.source.refresh is LoadState.NotLoading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(routeResultsFlow) {
            recyclerAdapter.submitData(PagingData.empty())
            recyclerAdapter.submitData(it)
        }
    }
}