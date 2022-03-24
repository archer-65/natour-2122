package com.unina.natourkt.feature_compilation.save_into_compilation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.adapter.CompilationDialogAdapter
import com.unina.natourkt.core.presentation.base.fragment.BaseDialogFragment
import com.unina.natourkt.core.presentation.model.CompilationDialogItemUi
import com.unina.natourkt.core.presentation.util.UiEffect
import com.unina.natourkt.core.presentation.util.asString
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.databinding.DialogCompilationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveIntoCompilationDialog :
    BaseDialogFragment<DialogCompilationBinding, SaveIntoCompilationViewModel>(),
    CompilationDialogAdapter.OnItemCLickListener {

    private val dialogAdapter = CompilationDialogAdapter(this@SaveIntoCompilationDialog)

    private val viewModel: SaveIntoCompilationViewModel by viewModels()

    override fun getViewBinding() = DialogCompilationBinding.inflate(layoutInflater)
    override fun getVM() = viewModel

    override var shouldSetCustomView: Boolean = true

    override var baseTitle = R.string.save_in_compilation_dialog_title
    override fun getDialogTitle() = baseTitle

    override var baseMessage = R.string.save_in_compilation_dialog_message
    override fun getDialogMessage() = baseMessage

    override fun getDialogIcon() = R.drawable.ic_outline_bookmark_24

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
    }

    private fun initRecycler() {
        binding.recyclerCompilations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dialogAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun collectState() {
        collectLatestOnLifecycleScope(viewModel.uiState) {
            dialogAdapter.submitList(it.compilations)
        }

        collectLatestOnLifecycleScope(viewModel.eventFlow) { event ->
            when (event) {
                is UiEffect.DismissDialog -> dismiss()
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
            }
        }
    }

    override fun onCompilationSelection(compilation: CompilationDialogItemUi) {
        viewModel.onEvent(SaveIntoCompilationEvent.OnSave(compilation.id))
    }
}