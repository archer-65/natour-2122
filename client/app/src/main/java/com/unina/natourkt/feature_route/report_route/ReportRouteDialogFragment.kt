package com.unina.natourkt.feature_route.report_route

import androidx.fragment.app.viewModels
import com.unina.natourkt.core.presentation.base.fragment.BaseDialogFragment
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.databinding.DialogReportRouteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportRouteDialogFragment :
    BaseDialogFragment<DialogReportRouteBinding, ReportRouteViewModel>() {

    private val viewModel: ReportRouteViewModel by viewModels()

    override fun getViewBinding() = DialogReportRouteBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override fun setupUi() {
        binding.toolbar.setTopMargin()
    }

    override fun setListeners() {
        binding.toolbar.setNavigationOnClickListener { dismiss() }
        binding.toolbar.setOnMenuItemClickListener { dismiss(); true }
    }
}