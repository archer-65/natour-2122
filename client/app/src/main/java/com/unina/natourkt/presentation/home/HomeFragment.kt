package com.unina.natourkt.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.common.scrollBehavior
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentHomeBinding
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.PostAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.model.PostItemUiState
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the home screen
 * filled of paginated posts
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    PostAdapter.OnItemClickListener {

    private val recyclerAdapter = PostAdapter(this@HomeFragment)

    private val viewModel: HomeViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun setListeners() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            recyclerAdapter.refresh()
        }

        newPostFab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_newPostFragment)
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerHome.apply {
                layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())
                adapter = initConcatAdapter()
                scrollBehavior(newPostFab)
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
            recyclerHome.isVisible = loadState.source.refresh !is LoadState.Loading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(postsFlow) {
            recyclerAdapter.submitData(it)
        }
    }

    override fun onItemClick(post: PostItemUiState) {
        val action = HomeFragmentDirections.actionHomeToPostDetails(
            post.id!!,
            post.authorId
        )
        findNavController().navigate(action)
    }
}