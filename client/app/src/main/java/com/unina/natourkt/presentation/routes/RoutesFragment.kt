package com.unina.natourkt.presentation.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentRoutesBinding
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.PostAdapter
import com.unina.natourkt.presentation.base.adapter.RouteAdapter
import com.unina.natourkt.presentation.home.HomeUiState
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This Fragment represents the routes screen
 * filled of paginated routes
 */
@AndroidEntryPoint
class RoutesFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentRoutesBinding? = null
    private val binding get() = _binding!!

    // Recycler elements
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RouteAdapter
    private lateinit var shimmerFrame: ShimmerFrameLayout
    //private lateinit var refresh: SwipeRefreshLayout

    // ViewModel
    private val routesViewModel: RoutesViewModel by viewModels()

    // Coroutines
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        initRecycler()
        handleFab()
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

        recyclerView = binding.recyclerRoutes

        shimmerFrame = binding.shimmerContainer

        //refresh = binding.swipeRefresh
    }

    /**
     * Recycler View init function
     */
    private fun initRecycler() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoutesFragment.requireContext())
            recyclerAdapter = RouteAdapter()
            adapter = recyclerAdapter.withLoadStateHeaderAndFooter(
                header = ItemLoadStateAdapter(),
                footer = ItemLoadStateAdapter()
            )
        }
        recyclerAdapter.addLoadStateListener { loadState ->
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

    private fun handleFab() = with(binding) {
        recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                newRouteFab.hide()
            } else if (scrollX == scrollY) {
                newRouteFab.show()
            } else {
                newRouteFab.show()
            }
        }
    }

    private fun setListeners() = with(binding) {
//        refresh.setOnRefreshListener {
//            recyclerAdapter.refresh()
//            refresh.isRefreshing = false
//        }

        newRouteFab.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.newRouteFab to "transitionNewRouteFab")
            findNavController().navigate(R.id.action_navigation_routes_to_navigation_new_route_flow, null, null, extras)
        }
    }

    /**
     * Start to collect [RouteUiState], action based on Success/Loading/Error
     */
    private fun collectState() {

        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                routesViewModel.uiState.collectLatest { uiState ->
                    // Send data to adapter
                    recyclerAdapter.submitData(uiState.routeItems)
                }
            }
        }
    }
}