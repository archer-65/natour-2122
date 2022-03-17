package com.unina.natourkt.feature_route.routes

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.CompilationDialogAdapter
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.RouteAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.CompilationDialogItemUi
import com.unina.natourkt.core.presentation.model.RouteItemUi
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.scrollBehavior
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.databinding.FragmentRoutesBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Fragment represents the routes screen
 * filled of paginated routes
 */
@AndroidEntryPoint
class RoutesFragment : BaseFragment<FragmentRoutesBinding, RoutesViewModel>(),
    RouteAdapter.OnItemClickListener, CompilationDialogAdapter.OnItemCLickListener {

    private val recyclerAdapter = RouteAdapter(this@RoutesFragment)
    private val dialogAdapter = CompilationDialogAdapter(this@RoutesFragment)

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var dialog: AlertDialog
    private lateinit var customAlertDialogView: View

    private val viewModel: RoutesViewModel by viewModels()

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRoutesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())

        setListeners()
        initRecycler()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
    }

    override fun setListeners() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                recyclerAdapter.refresh()
            }

            newRouteFab.setOnClickListener {
                val extras = FragmentNavigatorExtras(newRouteFab to "transitionNewRouteFab")
                findNavController().navigate(
                    R.id.action_routes_to_create_route_flow,
                    null,
                    null,
                    extras
                )
            }

            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search_route -> {
                        findNavController().navigate(R.id.action_routes_to_route_search)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerRoutes.apply {
                layoutManager = LinearLayoutManager(this@RoutesFragment.requireContext())
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

            swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading

            shimmerContainer.isVisible = loadState.source.refresh is LoadState.Loading
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

        collectLatestOnLifecycleScope(uiState) {
            dialogAdapter.submitList(it.compilations)
        }

        collectLatestOnLifecycleScope(eventFlow) { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Snackbar.make(
                        binding.newRouteFab,
                        event.uiText.asString(requireContext()),
                        Snackbar.LENGTH_SHORT
                    ).setAnchorView(binding.newRouteFab).show()
                }
                is UiEvent.DismissDialog -> dialog.dismiss()
            }
        }
    }

    override fun onItemClick(route: RouteItemUi) {
        val action = RoutesFragmentDirections.actionRoutesToRouteDetails(
            route.id,
            route.authorId
        )
        findNavController().navigate(action)
    }

    private fun launchDialog() {
        customAlertDialogView =
            layoutInflater.inflate(R.layout.dialog_compilation, binding.root, false)
        val recyclerDialog: RecyclerView =
            customAlertDialogView.findViewById(R.id.recycler_compilations)

        recyclerDialog.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dialogAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        dialog = materialAlertDialogBuilder.setView(customAlertDialogView)
            .setTitle("Salva itinerario")
            .setMessage("Seleziona una compilation dove salvare questo itinerario")
            .create()

        dialog.show()
    }

    override fun onSaveClick(route: RouteItemUi) {
        viewModel.getCompilationsToSave(route.id)
        launchDialog()
    }

    override fun onCompilationSelection(compilation: CompilationDialogItemUi) {
        Log.i("STO QUA", "ciao")
        viewModel.saveRouteIntoCompilation(compilation.id)
    }
}