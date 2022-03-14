package com.unina.natourkt.presentation.route_details.tag

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.common.Constants
import com.unina.natourkt.databinding.FragmentRouteDetailsTagBinding
import com.unina.natourkt.presentation.base.adapter.ItemLoadStateAdapter
import com.unina.natourkt.presentation.base.adapter.PostGridAdapter
import com.unina.natourkt.presentation.base.decoration.GridItemDecoration
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.model.PostGridItemUiState
import com.unina.natourkt.presentation.route_details.RouteDetailsFragmentDirections
import com.unina.natourkt.presentation.route_details.RouteDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteDetailsTagFragment :
    BaseFragment<FragmentRouteDetailsTagBinding, RouteDetailsViewModel>(),
    PostGridAdapter.OnItemClickListener {

    private val recyclerAdapter = PostGridAdapter(this@RouteDetailsTagFragment)
    private val footerLoadStateAdapter = ItemLoadStateAdapter()
    private val headerLoadStateAdapter = ItemLoadStateAdapter()

    private val viewModel: RouteDetailsViewModel by hiltNavGraphViewModels(R.id.navigation_route_details_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRouteDetailsTagBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun initRecycler() {
        with(binding) {
            recyclerTaggedPosts.apply {
                // Grid declaration with Columns count
                val gridAdapter = initConcatAdapter()

                val grid = GridLayoutManager(
                    this@RouteDetailsTagFragment.requireContext(),
                    Constants.COLUMN_COUNT
                )

                grid.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == gridAdapter.itemCount - 1 && footerLoadStateAdapter.itemCount > 0 || headerLoadStateAdapter.itemCount > 0) {
                            Constants.COLUMN_COUNT
                        } else {
                            1
                        }
                    }
                }

                layoutManager = grid

                addItemDecoration(
                    GridItemDecoration(
                        Constants.COLUMN_COUNT,
                        Constants.COLUMN_SPACING,
                        false
                    )
                )

                adapter = gridAdapter

            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            //recyclerTaggedPosts.isVisible = loadState.source.refresh !is LoadState.Loading
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

    override fun onItemClick(post: PostGridItemUiState) {
        val action = RouteDetailsFragmentDirections.actionGlobalNavigationPostDetails(
            post.id,
            post.authorId
        )
        findNavController().navigate(action)
    }
}