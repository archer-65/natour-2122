package com.unina.natourkt.feature_route.new_route

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRouteInfoBinding
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.util.DurationFilter
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.core.presentation.util.setTopMargin
import com.unina.natourkt.core.presentation.util.updateText

class NewRouteInfoFragment : BaseFragment<FragmentNewRouteInfoBinding, NewRouteViewModel>() {

    private val viewModel: NewRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentNewRouteInfoBinding.inflate(layoutInflater)

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

    override fun setupUi() {
        with(binding) {
            topAppBar.setTopMargin()

            nextFab.setBottomMargin()

            durationTextField.editText?.apply {
                filters = arrayOf<InputFilter>(DurationFilter(1, 16))
            }
        }
    }

    override fun setListeners() = with(binding) {
        nextFab.setOnClickListener {
            findNavController().navigate(R.id.action_new_route_info_to_new_route_map)
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
                setTitle(it)
            }

            descriptionTextField.updateText {
                setDescription(it)
            }

            durationTextField.updateText {
                setDuration(it)
            }

            disabilityFriendlySwitch.setOnCheckedChangeListener { compoundButton, _ ->
                setDisabilityFriendly(compoundButton.isChecked)
            }

            difficultyChipgroup.setOnCheckedStateChangeListener { group, _ ->
                val checkedDifficulty = when (group.checkedChipId) {
                    easyChip.id -> Difficulty.EASY
                    mediumChip.id -> Difficulty.MEDIUM
                    hardChip.id -> Difficulty.HARD
                    else -> Difficulty.EASY
                }
                setDifficulty(checkedDifficulty)
            }
        }
    }

    override fun collectState() = with(binding) {
        with(viewModel) {
            bindInfo(uiState.value.routeInfo)

            collectOnLifecycleScope(uiState) {
                nextFab.isEnabled =
                    it.routeInfo.routeTitle.isNotBlank() && it.routeInfo.duration.isNotBlank()
            }
        }
    }

    private fun bindInfo(route: NewRouteInfo) = with(binding) {
        routeTitleTextField.editText?.setText(route.routeTitle)
        descriptionTextField.editText?.setText(route.routeDescription)
        difficultyChipgroup.check(
            when (route.difficulty) {
                Difficulty.EASY -> easyChip.id
                Difficulty.MEDIUM -> mediumChip.id
                Difficulty.HARD -> hardChip.id
            }
        )
        durationTextField.editText?.setText(route.duration)
        disabilityFriendlySwitch.isChecked = route.disabilityFriendly
    }
}
