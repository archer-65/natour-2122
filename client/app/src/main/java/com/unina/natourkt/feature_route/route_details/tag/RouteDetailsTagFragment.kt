package com.unina.natourkt.feature_route.route_details.tag

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.PostGridAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.decoration.GridItemDecoration
import com.unina.natourkt.core.presentation.model.PostGridItemUi
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.databinding.FragmentRouteDetailsTagBinding
import com.unina.natourkt.feature_route.route_details.RouteDetailsEvent
import com.unina.natourkt.feature_route.route_details.RouteDetailsFragmentDirections
import com.unina.natourkt.feature_route.route_details.RouteDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteDetailsTagFragment :
    BaseFragment<FragmentRouteDetailsTagBinding, RouteDetailsViewModel>(),
    PostGridAdapter.OnItemClickListener {

    private val recyclerAdapter = PostGridAdapter(this@RouteDetailsTagFragment)
    private val footerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)
    private val headerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)

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
        val action = RouteDetailsFragmentDirections.actionGlobalNavigationPostDetails(
            post.id,
            post.authorId
        )

        viewModel.onEvent(RouteDetailsEvent.ClickPost)

        findNavController().navigate(action)
    }
}