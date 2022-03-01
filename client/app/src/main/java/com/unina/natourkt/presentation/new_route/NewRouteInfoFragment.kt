package com.unina.natourkt.presentation.new_route

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRouteInfoBinding
import com.unina.natourkt.presentation.base.fragment.BaseFragment
import com.unina.natourkt.presentation.base.input_filter.DurationFilter
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewRouteInfoFragment : BaseFragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentNewRouteInfoBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val newRouteViewModel: NewRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewRouteInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        setTextChangedListeners()
        collectState()
    }

    override fun setupUi() {
        with(binding) {
            topAppBar.applyInsetter {
                type(statusBars = true) {
                    margin()
                }
            }

            nextFab.applyInsetter {
                type(navigationBars = true) {
                    margin()
                }
            }

            durationTextField.editText?.apply {
                filters = arrayOf<InputFilter>(DurationFilter(1, 16))
            }
        }
    }

    override fun setListeners() = with(binding) {
        nextFab.setOnClickListener {
            // Prepare information before next screen
            prepareInfo(newRouteViewModel.uiState.value.routeInfo)
            findNavController().navigate(R.id.action_new_route_info_to_new_route_map)
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

    private fun setTextChangedListeners() = with(binding) {
        routeTitleTextField.editText?.doAfterTextChanged {
            isFormValidForButton()
        }
        durationTextField.editText?.doAfterTextChanged {
            isFormValidForButton()
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

        newRouteViewModel.setInfo(newRoute)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newRouteViewModel.uiState.collect() { uiState ->
                    uiState.apply {
                        bindInfo(routeInfo)
                    }
                }
            }
        }
    }
}