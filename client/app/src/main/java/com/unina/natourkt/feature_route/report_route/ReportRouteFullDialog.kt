package com.unina.natourkt.feature_route.report_route

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.core.presentation.base.fragment.BaseFullDialogFragment
import com.unina.natourkt.core.presentation.util.UiEvent
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.core.presentation.util.updateText
import com.unina.natourkt.databinding.DialogReportRouteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportRouteFullDialog :
    BaseFullDialogFragment<DialogReportRouteBinding, ReportRouteViewModel>() {

    private val viewModel: ReportRouteViewModel by viewModels()

    override fun getViewBinding() = DialogReportRouteBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun setupUi() {
        binding.toolbar.setTopMargin()
    }

    override fun setListeners() {
        binding.toolbar.setNavigationOnClickListener { dismiss() }

        binding.sendReportButton.setOnClickListener {
            viewModel.onEvent(ReportRouteEvent.Upload)
        }
    }

    override fun setTextChangedListeners() {
        binding.reportTitleTextField.updateText {
            viewModel.onEvent(ReportRouteEvent.EnteredTitle(it))
        }

        binding.descriptionTextField.updateText {
            viewModel.onEvent(ReportRouteEvent.EnteredDescription(it))
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                if (it.isInserted) {
                    dismiss()
                }

                sendReportButton.isEnabled = it.isButtonEnabled
                progressBar.isVisible = it.isLoading
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        Snackbar.make(
                            requireView(),
                            event.uiText.asString(requireContext()),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
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
}