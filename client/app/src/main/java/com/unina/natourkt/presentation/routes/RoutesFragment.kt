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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.databinding.FragmentRoutesBinding
import com.unina.natourkt.presentation.base.adapter.PostAdapter
import com.unina.natourkt.presentation.base.adapter.RouteAdapter
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoutesFragment : Fragment() {

    private var _binding: FragmentRoutesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Recycler
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RouteAdapter

    // ProgressBar
    private lateinit var progressBar: ProgressBar

    private val routesViewModel: RoutesViewModel by viewModels()

    // Coroutines
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUi()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        collectState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {
        binding.topAppBar.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        recyclerView = binding.recyclerRoutes

        progressBar = binding.progressBar
    }

    private fun initRecycler() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RoutesFragment.requireContext())
            recyclerAdapter = RouteAdapter()
            // ADD HEADER AND FOOTER
            adapter = recyclerAdapter
        }
        recyclerAdapter.addLoadStateListener { loadState ->
            recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        }
    }

    private fun collectState() {

        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                routesViewModel.uiState.collectLatest { uiState ->
                    uiState.routeItems?.let { recyclerAdapter.submitData(it) }
                }
            }
        }
    }
}