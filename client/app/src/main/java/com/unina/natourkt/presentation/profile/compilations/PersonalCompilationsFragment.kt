package com.unina.natourkt.presentation.profile.compilations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentPersonalCompilationsBinding
import com.unina.natourkt.databinding.FragmentPersonalPostsBinding
import com.unina.natourkt.presentation.base.adapter.CompilationAdapter
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.PostGridAdapter
import com.unina.natourkt.presentation.base.adapter.RouteAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.profile.posts.PersonalPostsViewModel
import com.unina.natourkt.presentation.routes.RouteUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This Fragment represents the profile compilations screen
 * filled of paginated compilations
 */
@AndroidEntryPoint
class PersonalCompilationsFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentPersonalCompilationsBinding? = null
    private val binding get() = _binding!!

    // Recycler elements
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: CompilationAdapter

    // ViewModel
    private val personalCompilationsViewModel: PersonalCompilationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalCompilationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
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
    override fun setupUi() {
        recyclerView = binding.recyclerCompilations
    }

    /**
     * Recycler View init function
     */
    override fun initRecycler() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PersonalCompilationsFragment.requireContext())
            recyclerAdapter = CompilationAdapter()
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
                newCompilationFab.hide()
            } else if (scrollX == scrollY) {
                newCompilationFab.show()
            } else {
                newCompilationFab.show()
            }
        }
    }

    override fun collectState() {
        with(personalCompilationsViewModel) {
            launchOnLifecycleScope {
                compilationsFlow.collectLatest {
                    // Send data to adapter
                    recyclerAdapter.submitData(it)
                }
            }
        }
    }
}