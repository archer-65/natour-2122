package com.unina.natourkt.feature_admin.report_details

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.databinding.FragmentReportDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportDetailsFragment : BaseFragment<FragmentReportDetailsBinding, ReportDetailsViewModel>() {

    private val viewModel: ReportDetailsViewModel by viewModels()

    override fun getViewBinding() = FragmentReportDetailsBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun setupUi() {
        binding.topAppBar.setTopMargin()
    }

    override fun setListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.deleteReportButton.setOnClickListener {
            showHelperDialog(
                title = R.string.delete_report_dialog_title,
                message = R.string.delete_report_dialog_message,
                icon = R.drawable.ic_outline_delete_24,
                positive = R.string.remove_dialog,
                negative = R.string.cancel_dialog
            ) {
                viewModel.deleteReport()
            }
        }

        binding.gotoRouteButton.setOnClickListener {
            val action =
                ReportDetailsFragmentDirections.actionReportDetailsFragmentToNavigationRouteDetailsFlow2(
                    viewModel.reportInfo.reportedRoute.routeId
                )
            findNavController().navigate(action)
        }
    }

    override fun collectState() = with(viewModel) {
        binding.reportTitleTextView.text = reportInfo.title
        binding.reportDescriptionTextView.text = reportInfo.description

        collectLatestOnLifecycleScope(viewModel.uiState) {
            if (it.isDeleted) {
                findNavController().navigate(R.id.action_reportDetailsFragment_to_navigation_admin_board)
            }
        }

        collectLatestOnLifecycleScope(viewModel.eventFlow) { event ->
            when (event) {
                is UiEvent.ShowToast -> {
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
}