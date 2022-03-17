package com.unina.natourkt.feature_profile.profile.compilations

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.CompilationAdapter
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.CompilationItemUi
import com.unina.natourkt.core.presentation.util.scrollBehavior
import com.unina.natourkt.databinding.FragmentPersonalCompilationsBinding
import com.unina.natourkt.feature_profile.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the profile compilations screen
 * filled of paginated compilations
 */
@AndroidEntryPoint
class ProfileCompilationsFragment :
    BaseFragment<FragmentPersonalCompilationsBinding, ProfileCompilationsViewModel>(),
    CompilationAdapter.OnItemClickListener {

    private val recyclerAdapter = CompilationAdapter(this@ProfileCompilationsFragment)

    private val viewModel: ProfileCompilationsViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentPersonalCompilationsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setListeners() = with(binding) {
        newCompilationFab.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_create_compilation)
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerCompilations.apply {
                layoutManager =
                    LinearLayoutManager(this@ProfileCompilationsFragment.requireContext())
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

    override fun onItemClick(compilation: CompilationItemUi) {
        val action = ProfileFragmentDirections.actionProfileToCompilationDetails(
            compilation
        )
        findNavController().navigate(action)
    }
}