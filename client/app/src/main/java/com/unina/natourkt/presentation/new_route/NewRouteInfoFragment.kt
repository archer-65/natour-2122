package com.unina.natourkt.presentation.new_route

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRouteInfoBinding
import com.unina.natourkt.databinding.FragmentRouteDetailsBinding
import com.unina.natourkt.presentation.base.input_filter.DurationFilter
import dev.chrisbanes.insetter.applyInsetter
import java.lang.NumberFormatException

class NewRouteInfoFragment : Fragment() {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentNewRouteInfoBinding? = null
    private val binding get() = _binding!!

    private val newRouteViewModel: NewRouteViewModel by activityViewModels()

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
    }

    private fun setupUi() = with(binding) {
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

    private fun setListeners() = with(binding) {
        nextFab.setOnClickListener {

            prepareInfo()

            findNavController().navigate(R.id.action_navigation_new_route_info_to_navigation_new_route_map)
        }

        disabilityFriendlySwitch.setOnCheckedChangeListener { check, state ->
            when (state) {
                true -> disabilityFriendlyTextviewSub.text =
                    "Accessibile a persone con disabilità"
                else -> disabilityFriendlyTextviewSub.text =
                    "Non accessibile a persone con disabilità"
            }
        }
    }

    private fun prepareInfo() = with(binding) {
        val title = routeTitleTextField.editText?.text.toString().trim()
        val description = descriptionTextField.editText?.text.toString().trim()
        val duration = durationTextField.editText?.text.toString().trim()
        val disabilityFriendly = disabilityFriendlySwitch.isChecked
        val difficulty = difficultyChipgroup.run {
            when (checkedChipId) {
                R.id.easyChip -> 1
                R.id.mediumChip -> 2
                R.id.hardChip -> 3
                else -> 0
            }
        }

        val routeInfo = NewRoute(
            routeTitle = title,
            routeDescription = description,
            duration = duration.toInt(),
            disabilityFriendly = disabilityFriendly,
            difficulty = difficulty,
        )
        newRouteViewModel.setInfo(routeInfo)
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
}