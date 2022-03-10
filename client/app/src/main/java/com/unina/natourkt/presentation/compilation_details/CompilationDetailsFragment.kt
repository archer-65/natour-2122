package com.unina.natourkt.presentation.compilation_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.common.loadWithGlide
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentCompilationDetailsBinding
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.RouteAdapter
import com.unina.natourkt.presentation.base.adapter.RouteCompilationAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.RouteItemUiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CompilationDetailsFragment :
    BaseFragment<FragmentCompilationDetailsBinding, CompilationDetailsViewModel>(),
    RouteCompilationAdapter.OnItemClickListener {

    private val recyclerAdapter = RouteCompilationAdapter(this@CompilationDetailsFragment)

    private val viewModel: CompilationDetailsViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentCompilationDetailsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        bindView()
    }

    override fun setupUi() {
        //binding.appBarLayout.setTopMargin()
    }

    override fun initRecycler() {
        with(binding) {
            recyclerCompilationRoutes.apply {
                layoutManager =
                    LinearLayoutManager(this@CompilationDetailsFragment.requireContext())
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

            recyclerCompilationRoutes.isVisible = loadState.source.refresh !is LoadState.Loading
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

    private fun bindView() = with(viewModel) {
        with(binding) {
            compilationPhoto.loadWithGlide(compilation?.photo)
            topAppBar.title = compilation?.title
        }
    }

    override fun onItemClick(route: RouteItemUiState) {
        val action =
            CompilationDetailsFragmentDirections.actionCompilationDetailsFragmentToNavigationRouteDetailsFlow(
                route.id,
                route.authorId
            )

        findNavController().navigate(action)
    }
}