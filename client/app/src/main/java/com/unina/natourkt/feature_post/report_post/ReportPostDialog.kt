package com.unina.natourkt.feature_post.report_post

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseDialogFragment
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.databinding.DialogReportPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportPostDialog : BaseDialogFragment<DialogReportPostBinding, ReportPostViewModel>() {

    private val viewModel: ReportPostViewModel by viewModels()

    override fun getViewBinding() = DialogReportPostBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override var baseTitle = R.string.send_report_dialog_title
    override fun getDialogTitle() = baseTitle

    override var baseMessage = R.string.send_report_dialog_message
    override fun getDialogMessage() = baseMessage

    override fun getDialogIcon() = R.drawable.ic_baseline_error_outline_24
    override fun getDialogPositive() = R.string.report_action_dialog
    override fun getDialogNegative() = R.string.cancel_dialog

    override fun positiveAction() {
        viewModel.sendReport()
    }

    override fun negativeAction() {
        findNavController().navigateUp()
    }

    override fun collectState() {
        collectLatestOnLifecycleScope(viewModel.uiState) {
            if (it.isReported) {
                if (findNavController().previousBackStackEntry?.destination?.id == R.id.bottomSheetHomeFragment2) {
                    findNavController().navigateUp()
                }

                findNavController().navigateUp()
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