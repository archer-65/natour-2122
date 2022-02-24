package com.unina.natourkt.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.unina.natourkt.databinding.FragmentHomeBinding
import com.unina.natourkt.presentation.base.adapter.PostAdapter
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.PostItemUiState
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * This Fragment represents the home screen
 * filled of paginated posts
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment(), PostAdapter.OnItemClickListener {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Recycler elements
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: PostAdapter
    private lateinit var shimmerFrame: ShimmerFrameLayout
    private lateinit var refresh: SwipeRefreshLayout

    // ViewModel
    private val homeViewModel: HomeViewModel by viewModels()

    // Coroutines
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUi()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        setListeners()

        collectState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Basic settings for UI
     */
    private fun setupUi() {
        binding.topAppBar.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        refresh = binding.swipeRefresh

        recyclerView = binding.recyclerHome

        shimmerFrame = binding.shimmerContainer
    }

    /**
     * Recycler View init function
     */
    private fun initRecycler() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.requireContext())
            recyclerAdapter = PostAdapter(this@HomeFragment)

            adapter = recyclerAdapter.withLoadStateHeaderAndFooter(
                header = ItemLoadStateAdapter(),
                footer = ItemLoadStateAdapter()
            )
        }



        recyclerAdapter.addLoadStateListener { loadState ->

            refresh.isRefreshing = loadState.source.refresh is LoadState.Loading

            when (loadState.source.refresh) {
                // If loading, start the shimmer animation and mark as GONE the Recycler
                is LoadState.Loading -> {
                    shimmerFrame.startShimmer()
                    shimmerFrame.isVisible = true
                    recyclerView.isVisible = false
                }
                // If not loading, stop the shimmer animation and mark as VISIBLE the Recycler
                else -> {
                    shimmerFrame.stopShimmer()
                    shimmerFrame.isVisible = false
                    recyclerView.isVisible = true
                }
            }
        }
    }

    fun setListeners() {
        refresh.setOnRefreshListener {
            recyclerAdapter.refresh()
        }
    }

    /**
     * Start to collect [HomeUiState], action based on Success/Loading/Error
     */
    private fun collectState() {

        // Make sure to cancel any previous job
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collectLatest { uiState ->
                    // Send data to adapter
                    recyclerAdapter.submitData(uiState.postItems)
                }

                recyclerAdapter.loadStateFlow.collectLatest { }
            }
        }
    }

    override fun onItemClick(post: PostItemUiState) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationViewerPost(
            post.id,
            post.authorId
        )
        findNavController().navigate(action)
    }
}