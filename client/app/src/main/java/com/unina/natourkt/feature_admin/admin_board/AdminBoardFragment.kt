package com.unina.natourkt.feature_admin.admin_board

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.core.presentation.adapter.ItemLoadStateAdapter
import com.unina.natourkt.core.presentation.adapter.ReportAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.ReportItemUi
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.databinding.FragmentAdminBoardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminBoardFragment : BaseFragment<FragmentAdminBoardBinding, AdminBoardViewModel>(),
    ReportAdapter.OnItemClickListener {

    private val recyclerAdapter = ReportAdapter(this@AdminBoardFragment)

    private val viewModel: AdminBoardViewModel by viewModels()

    override fun getViewBinding() = FragmentAdminBoardBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun setupUi() {
        binding.topAppBar.setTopMargin()
    }

    override fun setListeners() {
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                recyclerAdapter.refresh()
            }
        }
    }

    override fun initRecycler() {
        with(binding) {
            recyclerReports.apply {
                layoutManager = LinearLayoutManager(this@AdminBoardFragment.requireContext())
                adapter = initConcatAdapter()
            }
        }
    }

    override fun initConcatAdapter(): ConcatAdapter = with(binding) {
        val footerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)
        val headerLoadStateAdapter = ItemLoadStateAdapter(recyclerAdapter::retry)

        recyclerAdapter.addLoadStateListener { loadState ->
            footerLoadStateAdapter.loadState = loadState.append
            headerLoadStateAdapter.loadState = loadState.refresh

            swipeRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading

            shimmerContainer.isVisible = loadState.source.refresh is LoadState.Loading
            recyclerReports.isVisible = loadState.source.refresh !is LoadState.Loading
        }

        val concatAdapter =
            ConcatAdapter(headerLoadStateAdapter, recyclerAdapter, footerLoadStateAdapter)
        return concatAdapter
    }

    override fun collectState() = with(viewModel) {
        collectLatestOnLifecycleScope(reportsFlow) {
            recyclerAdapter.submitData(it)
        }

        collectLatestOnLifecycleScope(eventFlow) { event ->
            when (event) {
                is UiEffect.ShowSnackbar -> {
                    Snackbar.make(
                        requireView(),
                        event.uiText.asString(requireContext()),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is UiEffect.ShowToast -> {
                    Toast.makeText(
                        requireContext(),
                        event.uiText.asString(requireContext()),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }
    }

    override fun onItemClick(report: ReportItemUi) {
        val action = AdminBoardFragmentDirections.actionNavigationAdminBoardToReportDetailsFragment(
            report
        )

        findNavController().navigate(action)
    }
}

