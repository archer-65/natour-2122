package com.unina.natourkt.presentation.profile.compilations

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.common.scrollBehavior
import com.unina.natourkt.databinding.FragmentPersonalCompilationsBinding
import com.unina.natourkt.presentation.base.adapter.CompilationAdapter
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**
 * This Fragment represents the profile compilations screen
 * filled of paginated compilations
 */
@AndroidEntryPoint
class PersonalCompilationsFragment :
    BaseFragment<FragmentPersonalCompilationsBinding, PersonalCompilationsViewModel>() {

    private val recyclerAdapter = CompilationAdapter()

    private val viewModel: PersonalCompilationsViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentPersonalCompilationsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
    }

    override fun initRecycler() {
        with(binding) {
            recyclerCompilations.apply {
                layoutManager =
                    LinearLayoutManager(this@PersonalCompilationsFragment.requireContext())
                adapter = initConcatAdapter()
                scrollBehavior(newCompilationFab)
            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {
        val footerLoadStateAdapter = ItemLoadStateAdapter()
        val headerLoadStateAdapter = ItemLoadStateAdapter()

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            recyclerCompilations.isVisible = loadState.source.refresh !is LoadState.Loading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(compilationsFlow) {
            recyclerAdapter.submitData(it)
        }
    }
}