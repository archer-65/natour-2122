package com.unina.natourkt.feature_route.create_route.info

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.databinding.FragmentCreateRouteInfoBinding
import com.unina.natourkt.feature_route.create_route.CreateRouteEvent
import com.unina.natourkt.feature_route.create_route.CreateRouteViewModel

class CreateRouteInfoFragment :
    BaseFragment<FragmentCreateRouteInfoBinding, CreateRouteViewModel>() {

    private val viewModel: CreateRouteViewModel by hiltNavGraphViewModels(R.id.navigation_create_route_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentCreateRouteInfoBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        setTextChangedListeners()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
        nextFab.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        nextFab.setOnClickListener {
            findNavController().navigate(R.id.action_create_route_info_to_create_route_map)
        }

        durationTextField.editText?.apply {
            filters = arrayOf<InputFilter>(DurationFilter(1, 16))
        }

        disabilityFriendlySwitch.setOnCheckedChangeListener { _, state ->
            when (state) {
                true -> disabilityFriendlyTextviewSub.text =
                    getString(R.string.disability_access)
                else -> disabilityFriendlyTextviewSub.text =
                    getString(R.string.disability_access_denied)
            }
        }

        topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun setTextChangedListeners() = with(binding) {
        with(viewModel) {
            routeTitleTextField.updateText {
                onEvent(CreateRouteEvent.EnteredTitle(it))
            }

            descriptionTextField.updateText {
                onEvent(CreateRouteEvent.EnteredDescription(it))
            }

            durationTextField.updateText {
                onEvent(CreateRouteEvent.EnteredDuration(it))
            }

            disabilityFriendlySwitch.setOnCheckedChangeListener { switch, _ ->
                onEvent(CreateRouteEvent.EnteredDisability(switch.isChecked))
            }

            difficultyChipgroup.setOnCheckedStateChangeListener { group, _ ->
                val checkedDifficulty = when (group.checkedChipId) {
                    easyChip.id -> Difficulty.EASY
                    mediumChip.id -> Difficulty.MEDIUM
                    hardChip.id -> Difficulty.HARD
                    else -> Difficulty.EASY
                }
                onEvent(CreateRouteEvent.EnteredDifficulty(checkedDifficulty))
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            bindInfo(uiStateInfo.value)

            collectOnLifecycleScope(uiStateInfo) {
                nextFab.isEnabled = it.isButtonEnabled
            }

            collectLatestOnLifecycleScope(eventFlow) { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        Snackbar.make(
                            nextFab,
                            event.uiText.asString(requireContext()),
                            Snackbar.LENGTH_SHORT
                        ).setAnchorView(nextFab).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun bindInfo(route: CreateRouteInfoUiState) = with(binding) {
        routeTitleTextField.editText?.setText(route.routeTitle.text)
        descriptionTextField.editText?.setText(route.routeDescription.text)
        difficultyChipgroup.check(
            when (route.difficulty) {
                Difficulty.EASY -> easyChip.id
                Difficulty.MEDIUM -> mediumChip.id
                Difficulty.HARD -> hardChip.id
                else -> easyChip.id
            }
        )
        durationTextField.editText?.setText(route.duration.text)
        disabilityFriendlySwitch.isChecked = route.disabilityFriendly
    }
}
