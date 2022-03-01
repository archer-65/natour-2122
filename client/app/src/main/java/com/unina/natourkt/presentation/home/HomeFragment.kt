package com.unina.natourkt.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.common.scrollBehavior
import com.unina.natourkt.databinding.FragmentHomeBinding
import com.unina.natourkt.presentation.base.adapter.PostAdapter
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.*

/**
 * This Fragment represents the home screen
 * filled of paginated posts
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment(), PostAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val recyclerAdapter = PostAdapter(this@HomeFragment)

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        initRecycler()
        collectState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setupUi() {
        with(binding) {
            topAppBar.applyInsetter {
                type(statusBars = true) {
                    margin()
                }
            }
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

    override fun setListeners() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            recyclerAdapter.refresh()
        }
    }

    override fun collectState() = with(homeViewModel) {
        launchOnLifecycleScope {
            postsFlow.collectLatest {
                recyclerAdapter.submitData(it)
            }
        }
    }

    override fun onItemClick(post: PostItemUiState) {
        val action = HomeFragmentDirections.actionHomeToPostDetails(
            post.id,
            post.authorId
        )
        findNavController().navigate(action)
    }
}