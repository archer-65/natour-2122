package com.unina.natourkt.feature_profile.profile.posts

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.PostGridAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.decoration.GridItemDecoration
import com.unina.natourkt.core.presentation.model.PostGridItemUi
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.presentation.util.scrollBehavior
import com.unina.natourkt.core.util.Constants.COLUMN_COUNT
import com.unina.natourkt.core.util.Constants.COLUMN_SPACING
import com.unina.natourkt.databinding.FragmentPersonalPostsBinding
import com.unina.natourkt.feature_profile.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the profile posts screen
 * filled of paginated posts
 */
@AndroidEntryPoint
class ProfilePostsFragment : BaseFragment<FragmentPersonalPostsBinding, ProfilePostsViewModel>(),
    PostGridAdapter.OnItemClickListener {

    private val recyclerAdapter = PostGridAdapter(this@ProfilePostsFragment)
    private val footerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)
    private val headerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)

    private val viewModel: ProfilePostsViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentPersonalPostsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setListeners() = with(binding) {
        newPostFab.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_create_post)
        }
    }

    /**
     * Recycler View init function
     */
    override fun initRecycler() {
        with(binding) {
            recyclerProfilePosts.apply {
                // Grid declaration with Columns count
                val gridAdapter = initConcatAdapter()

                val grid = GridLayoutManager(
                    this@ProfilePostsFragment.requireContext(),
                    COLUMN_COUNT
                )

                grid.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == gridAdapter.itemCount - 1 && footerLoadStateAdapter.itemCount > 0 || headerLoadStateAdapter.itemCount > 0) {
                            COLUMN_COUNT
                        } else {
                            1
                        }
                    }
                }

                layoutManager = grid

                addItemDecoration(GridItemDecoration(COLUMN_COUNT, COLUMN_SPACING, false))

                adapter = gridAdapter

                scrollBehavior(newPostFab)
            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            recyclerProfilePosts.isVisible = loadState.source.refresh !is LoadState.Loading
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

    override fun onItemClick(post: PostGridItemUi) {
        val action = ProfileFragmentDirections.actionProfileToPostDetails(
            post.id,
            post.authorId
        )
        findNavController().navigate(action)
    }
}