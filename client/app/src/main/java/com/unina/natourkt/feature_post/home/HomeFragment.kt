package com.unina.natourkt.feature_post.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.PostAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.PostItemUi
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.scrollBehavior
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.databinding.FragmentHomeBinding
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
            findNavController().navigate(R.id.action_home_to_create_post)
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
        val footerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)
        val headerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)

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

    override fun onItemClick(post: PostItemUi) {
        val action = HomeFragmentDirections.actionHomeToPostDetails(
            post.id,
            post.authorId
        )

        viewModel.onEvent(HomeEvent.ClickPost)

        findNavController().navigate(action)
    }
}