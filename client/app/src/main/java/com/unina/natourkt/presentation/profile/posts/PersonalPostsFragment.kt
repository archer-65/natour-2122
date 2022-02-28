package com.unina.natourkt.presentation.profile.posts

import android.os.Bundle
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unina.natourkt.common.Constants.COLUMN_COUNT
import com.unina.natourkt.common.Constants.COLUMN_SPACING
import com.unina.natourkt.databinding.FragmentPersonalPostsBinding
import com.unina.natourkt.presentation.base.decoration.GridItemDecoration
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.PostGridAdapter
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.ui_state.PostGridItemUiState
import com.unina.natourkt.presentation.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This Fragment represents the profile posts screen
 * filled of paginated posts
 */
@AndroidEntryPoint
class PersonalPostsFragment : BaseFragment(), PostGridAdapter.OnItemClickListener {

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

            recyclerAdapter = PostGridAdapter(this@PersonalPostsFragment)
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

    private fun handleFab() = with(binding) {
        recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                newPostFab.hide()
            } else if (scrollX == scrollY) {
                newPostFab.show()
            } else {
                newPostFab.show()
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
                    recyclerAdapter.submitData(uiState.postItems)
                }
            }
        }
    }

    override fun onItemClick(post: PostGridItemUiState) {
        val action = ProfileFragmentDirections.actionNavigationProfileToNavigationViewerPost(
            post.id,
            post.authorId
        )
        findNavController().navigate(action)
    }
}