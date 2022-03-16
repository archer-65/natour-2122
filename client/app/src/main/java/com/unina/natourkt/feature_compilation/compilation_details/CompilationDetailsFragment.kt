package com.unina.natourkt.feature_compilation.compilation_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.databinding.FragmentCompilationDetailsBinding
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.RouteCompilationAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.util.loadWithGlide
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

    override fun onItemClick(route: RouteItemUi) {
        val action =
            CompilationDetailsFragmentDirections.actionCompilationDetailsToRouteDetailsFlow(
                route.id,
                route.authorId
            )

        findNavController().navigate(action)
    }
}