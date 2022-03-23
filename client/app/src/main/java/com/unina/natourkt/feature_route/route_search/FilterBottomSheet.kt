package com.unina.natourkt.feature_route.route_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.contract.PlacesContract
import com.unina.natourkt.core.presentation.util.collectLatestOnLifecycleScope
import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.databinding.BottomSheetFilterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FilterBottomSheet : BottomSheetDialogFragment() {

    /**
     * Activity result launcher for `Places API`
     */
    private lateinit var launcherPlaces: ActivityResultLauncher<List<Place.Field>>

    private val viewModel: RouteSearchViewModel by hiltNavGraphViewModels(R.id.navigation_search_flow)

    private var _binding: BottomSheetFilterBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFilterBinding.inflate(inflater, container, false)
        initPlacesSearch()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setListeners()
        collectState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {

    }

    private fun setListeners() = with(binding) {
        with(viewModel)
        {
            areaChip.setOnClickListener {
                val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                launcherPlaces.launch(fields)
            }

            areaChip.setOnCloseIconClickListener {
                onEvent(RouteSearchEvent.FilterPlace(null))
            }

            areaSlider.addOnChangeListener { _, value, _ ->
                onEvent(RouteSearchEvent.FilterDistance(value))
            }

            durationChipGroup.setOnCheckedStateChangeListener { group, _ ->
                val minDuration: Int?
                val maxDuration: Int?

                when (group.checkedChipId) {
                    R.id.durationFirst -> {
                        minDuration = 1; maxDuration = 4
                    }
                    R.id.durationSecond -> {
                        minDuration = 4; maxDuration = 8
                    }
                    R.id.durationThird -> {
                        minDuration = 8; maxDuration = 12
                    }
                    R.id.durationFourth -> {
                        minDuration = 12; maxDuration = 16
                    }
                    else -> {
                        minDuration = null; maxDuration = null
                    }
                }

                onEvent(RouteSearchEvent.FilterDuration(minDuration, maxDuration))
            }

            difficultyChipgroup.setOnCheckedStateChangeListener { group, _ ->
                val difficulty: Difficulty

                when (group.checkedChipId) {
                    R.id.minEasyChip -> {
                        difficulty = Difficulty.EASY
                    }
                    R.id.minMediumChip -> {
                        difficulty = Difficulty.MEDIUM
                    }
                    R.id.minHardChip -> {
                        difficulty = Difficulty.HARD
                    }
                    else -> {
                        difficulty = Difficulty.NONE
                    }
                }

                onEvent(RouteSearchEvent.FilterDifficulty(difficulty))
            }

            disabilityFriendlyGroup.setOnCheckedStateChangeListener { group, _ ->
                val disability: Boolean?

                when (group.checkedChipId) {
                    R.id.accessible -> {
                        disability = true
                    }
                    R.id.notAccessible -> {
                        disability = false
                    }
                    else -> {
                        disability = null
                    }
                }

                onEvent(RouteSearchEvent.FilterDisability(disability))
            }

            buttonApply.setOnClickListener { dismiss() }
        }
    }

    private fun collectState() {
        collectLatestOnLifecycleScope(viewModel.uiState) {
            if (it.place != null) {
                binding.areaChip.text = it.place.name
                binding.areaChip.isCloseIconVisible = true
            } else {
                binding.areaChip.text = getString(R.string.choose_position)
                binding.areaChip.isCloseIconVisible = false
            }

            binding.areaSlider.isEnabled = it.place != null

            binding.areaSlider.value = it.distance
            binding.radiusTextView.text = it.distance.toInt().toString()

            bindDuration(it.minDuration, it.maxDuration)

            bindDifficulty(it.minDifficulty)

            bindDisability(it.isDisabilityFriendly)
        }
    }


    /**
     * Function to initialize [launcherPlaces] with [registerForActivityResult], replacing
     * [startActivityForResult] and [onActivityResult] (deprecated)
     * @see [PlacesContract]
     */
    private fun initPlacesSearch() {
        launcherPlaces = registerForActivityResult(PlacesContract()) { result ->
            result?.let {
                viewModel.onEvent(RouteSearchEvent.FilterPlace(it))
            }
        }
    }

    private fun bindDuration(minDuration: Int?, maxDuration: Int?) = with(binding) {
        if (minDuration == 1 && maxDuration == 4) {
            durationChipGroup.check(R.id.durationFirst)
        } else if (minDuration == 4 && maxDuration == 8) {
            durationChipGroup.check(R.id.durationSecond)
        } else if (minDuration == 8 && maxDuration == 12) {
            durationChipGroup.check(R.id.durationThird)
        } else if (minDuration == 12 && maxDuration == 16) {
            durationChipGroup.check(R.id.durationFourth)
        } else {
            durationChipGroup.clearCheck()
        }
    }

    private fun bindDifficulty(minDifficulty: Difficulty?) = with(binding) {
        when (minDifficulty) {
            Difficulty.EASY -> difficultyChipgroup.check(R.id.minEasyChip)
            Difficulty.MEDIUM -> difficultyChipgroup.check(R.id.minMediumChip)
            Difficulty.HARD -> difficultyChipgroup.check(R.id.minHardChip)
            else -> difficultyChipgroup.clearCheck()
        }
    }

    private fun bindDisability(disabilityFriendly: Boolean?) = with(binding) {
        when (disabilityFriendly) {
            true -> disabilityFriendlyGroup.check(R.id.accessible)
            false -> disabilityFriendlyGroup.check(R.id.notAccessible)
            else -> disabilityFriendlyGroup.clearCheck()
        }
    }
}