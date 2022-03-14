package com.unina.natourkt.feature_route.route_details.info

import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.unina.natourkt.R
import com.unina.natourkt.core.util.Difficulty
import com.unina.natourkt.core.presentation.base.fragment.BaseFragment
import com.unina.natourkt.core.presentation.model.RouteDetailsUi
import com.unina.natourkt.core.presentation.util.formatFull
import com.unina.natourkt.databinding.FragmentRouteDetailsInfoBinding
import com.unina.natourkt.feature_route.route_details.RouteDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteDetailsInfoFragment :
    BaseFragment<FragmentRouteDetailsInfoBinding, RouteDetailsViewModel>() {

    private val viewModel: RouteDetailsViewModel by hiltNavGraphViewModels(R.id.navigation_route_details_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentRouteDetailsInfoBinding.inflate(layoutInflater)

    override fun collectState() = with(binding) {
        with(viewModel) {
            collectLatestOnLifecycleScope(uiState) {
                chatButton.isVisible = it.loggedUser?.id != viewModel.authorId

                it.route?.let {
                    bindView(it)
                }

                progressBar.isVisible = it.isLoading
                constraintLayout.isVisible = !it.isLoading
            }
        }
    }

    private fun bindView(route: RouteDetailsUi) = with(binding) {
        routeTitleTextView.text = route.title
        routeDescription.text = route.description

        warningTextView.isVisible = route.modifiedDate != null || route.isReported

        if (route.modifiedDate == null && route.isReported) {
            warningTextView.text = "Informazioni inesatte o non aggiornate"
        } else if (route.modifiedDate != null) {
            warningTextView.text = route.modifiedDate.formatFull()
        }

        disabilityFriendlyTextView.isVisible = route.disabilityFriendly
        disabilityFriendlyImage.isVisible = route.disabilityFriendly

        routeImageSlider.apply {
            val imageList = route.photos.map { SlideModel(it) }
            setImageList(imageList, ScaleTypes.CENTER_CROP)
        }

        difficultyTextview.text = when (route.difficulty) {
            Difficulty.EASY -> "Facile"
            Difficulty.MEDIUM -> "Intermedio"
            Difficulty.HARD -> "Difficile"
        }

        durationTextView.text =
            getString(R.string.duration_placeholder_details, route.duration.toInt())
    }
}