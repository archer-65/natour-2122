package com.unina.natourkt.feature_profile.profile.routes

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.unina.natourkt.R
import com.unina.natourkt.core.util.scrollBehavior
import com.unina.natourkt.databinding.FragmentPersonalRoutesBinding
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.RouteAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.feature_profile.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the personal routes part of the profile screen
 * filled of paginated routes
 */
@AndroidEntryPoint
class PersonalRoutesFragment :
    BaseFragment<FragmentPersonalRoutesBinding, PersonalRoutesViewModel>(),
    RouteAdapter.OnItemClickListener {

    private val recyclerAdapter = RouteAdapter(this@PersonalRoutesFragment)

    private val viewModel: PersonalRoutesViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentPersonalRoutesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        initRecycler()
    }

    override fun setListeners() = with(binding) {
        newRouteFab.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_new_route_flow)
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerRoutes.apply {
                layoutManager = LinearLayoutManager(this@PersonalRoutesFragment.requireContext())
                adapter = initConcatAdapter()
                scrollBehavior(newRouteFab)
            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {
        val footerLoadStateAdapter = ItemLoadStateAdapter()
        val headerLoadStateAdapter = ItemLoadStateAdapter()

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            recyclerRoutes.isVisible = loadState.source.refresh !is LoadState.Loading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(routesFlow) {
            recyclerAdapter.submitData(it)
        }
    }

    override fun onItemClick(route: RouteItemUi) {
        val action = ProfileFragmentDirections.actionNavigationProfileToNavigationRouteDetails(
            route.id,
            route.authorId
        )
        findNavController().navigate(action)
    }
}