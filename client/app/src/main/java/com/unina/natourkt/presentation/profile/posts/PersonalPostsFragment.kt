package com.unina.natourkt.presentation.profile.posts

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.unina.natourkt.R
import com.unina.natourkt.common.Constants.COLUMN_COUNT
import com.unina.natourkt.common.Constants.COLUMN_SPACING
import com.unina.natourkt.databinding.FragmentHomeBinding
import com.unina.natourkt.databinding.FragmentPersonalPostsBinding
import com.unina.natourkt.presentation.base.adapter.GridItemDecoration
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.PostAdapter
import com.unina.natourkt.presentation.base.adapter.PostGridAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.home.HomeUiState
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This Fragment represents the profile posts screen
 * filled of paginated posts
 */
@AndroidEntryPoint
class PersonalPostsFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentPersonalPostsBinding? = null
    private val binding get() = _binding!!

    // Recycler elements
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: PostGridAdapter

    // ViewModel
    private val personalPostsViewModel: PersonalPostsViewModel by viewModels()

    // Coroutines
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPersonalPostsBinding.inflate(inflater, container, false)
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

    /**
     * Basic settings for UI
     */
    private fun setupUi() {

        recyclerView = binding.recyclerProfilePosts
    }

    /**
     * Recycler View init function
     */
    private fun initRecycler() {
        recyclerView.apply {
            // Grid declaration with Columns count
            val grid = GridLayoutManager(this@PersonalPostsFragment.requireContext(), COLUMN_COUNT)

            recyclerAdapter = PostGridAdapter()
            addItemDecoration(GridItemDecoration(COLUMN_COUNT, COLUMN_SPACING, false))

            val header = ItemLoadStateAdapter()
            val footer = ItemLoadStateAdapter()
            val gridAdapter = recyclerAdapter.withLoadStateHeaderAndFooter(
                header = header,
                footer = footer
            )
            adapter = gridAdapter

            layoutManager = grid
            grid.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == gridAdapter.itemCount - 1 && footer.itemCount > 0) {
                        COLUMN_COUNT
                    } else {
                        1
                    }
                }
            }
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

    /**
     * Start to collect [PersonalPostsUiState], action based on Success/Loading/Error
     */
    private fun collectState() {

        // Make sure to cancel any previous job
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                personalPostsViewModel.uiState.collectLatest { uiState ->
                    // Send data to adapter
                    uiState.postItems?.let { recyclerAdapter.submitData(it) }
                }
            }
        }
    }
}