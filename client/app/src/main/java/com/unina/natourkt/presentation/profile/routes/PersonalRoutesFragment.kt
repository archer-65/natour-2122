package com.unina.natourkt.presentation.profile.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentPersonalRoutesBinding
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.RouteAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

/**
 * This Fragment represents the personal routes part of the profile screen
 * filled of paginated routes
 */
@AndroidEntryPoint
class PersonalRoutesFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentPersonalRoutesBinding? = null
    private val binding get() = _binding!!

    // Recycler elements
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RouteAdapter

    // ViewModel
    private val personalRoutesViewModel: PersonalRoutesViewModel by viewModels()

    // Coroutines
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalRoutesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        initRecycler()
        handleFab()
        setListeners()
        collectState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Basic settings for Ui
     */
    override fun setupUi() {
        recyclerView = binding.recyclerRoutes
    }

    /**
     * Recycler View init function
     */
    override fun initRecycler() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PersonalRoutesFragment.requireContext())
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
                    recyclerView.isVisible = false
                }
                // If not loading, stop the shimmer animation and mark as VISIBLE the Recycler
                else -> {
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

    override fun setListeners() = with(binding) {
        newRouteFab.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.newRouteFab to "transitionNewRouteFab")
            findNavController().navigate(
                R.id.action_profile_to_new_route_flow,
                null,
                null,
                extras
            )
        }
    }

    /**
     * Start to collect [PersonalRoutesUiState], action based on Success/Loading/Error
     */
    override fun collectState() = with(personalRoutesViewModel) {
        launchOnLifecycleScope {
            routesFlow.collectLatest {
                // Send data to adapter
                recyclerAdapter.submitData(it)
            }
        }
    }
}