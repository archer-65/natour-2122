package com.unina.natourkt.presentation.new_route

import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.unina.natourkt.R
import com.unina.natourkt.common.setBottomMargin
import com.unina.natourkt.common.setTopMargin
import com.unina.natourkt.databinding.FragmentNewRouteInfoBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.input_filter.DurationFilter
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
            //Prepare information before next screen
            prepareInfo(viewModel.uiState.value.routeInfo)
            findNavController().navigate(R.id.action_new_route_info_to_new_route_map)
            //findNavController().navigate(R.id.action_navigation_new_route_info_to_newRoutePhotosFragment)
        }


        disabilityFriendlySwitch.setOnCheckedChangeListener { check, state ->
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

    override fun setTextChangedListeners() {
        with(binding) {
            routeTitleTextField.editText?.doAfterTextChanged {
                isFormValidForButton()
            }

            durationTextField.editText?.doAfterTextChanged {
                isFormValidForButton()
            }
        }
    }

    private fun isFormValidForButton() = with(binding) {
        nextFab.isEnabled =
            routeTitleTextField.editText?.text!!.isNotBlank() && durationTextField.editText?.text!!.isNotBlank()
    }

    private fun prepareInfo(route: NewRouteInfo) = with(binding) {
        val title = routeTitleTextField.editText?.text.toString().trim()
        val description = descriptionTextField.editText?.text.toString().trim()
        val duration = durationTextField.editText?.text.toString().trim()
        val disabilityFriendly = disabilityFriendlySwitch.isChecked
        val difficulty = difficultyChipgroup.run {
            when (checkedChipId) {
                easyChip.id -> Difficulty.EASY
                mediumChip.id -> Difficulty.MEDIUM
                hardChip.id -> Difficulty.HARD
                else -> Difficulty.EASY
            }
        }

        val newRoute = route.copy(
            routeTitle = title,
            routeDescription = description,
            duration = duration.toInt(),
            disabilityFriendly = disabilityFriendly,
            difficulty = difficulty,
        )

        viewModel.setInfo(newRoute)
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
        durationTextField.editText?.setText(route.duration.toString())
        disabilityFriendlySwitch.isChecked = route.disabilityFriendly
    }

    override fun collectState() {
        collectLatestOnLifecycleScope(viewModel.uiState) {
            bindInfo(it.routeInfo)
        }
    }
}
