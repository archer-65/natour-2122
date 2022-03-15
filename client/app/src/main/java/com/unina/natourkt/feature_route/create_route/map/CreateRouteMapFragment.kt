package com.unina.natourkt.feature_route.create_route.map

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.snackbar.Snackbar
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRouteMapBinding
import com.unina.natourkt.core.presentation.contract.GpxPickerContract
import com.unina.natourkt.core.presentation.base.fragment.BaseMapFragment
import com.unina.natourkt.core.presentation.util.*
import com.unina.natourkt.feature_route.create_route.CreateRouteEvent
import com.unina.natourkt.feature_route.create_route.CreateRouteViewModel

class CreateRouteMapFragment :
    BaseMapFragment<FragmentNewRouteMapBinding, CreateRouteViewModel, MapView>() {

    private lateinit var launcherGpx: ActivityResultLauncher<Unit>

    private val viewModel: CreateRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

    override fun getVM() = viewModel
    override fun getViewBinding() = FragmentNewRouteMapBinding.inflate(layoutInflater)
    override fun getMapBinding() = binding.mapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGpx()
    }

    override fun setupUi() = with(binding) {
        topAppBar.setTopMargin()
        mapView.setBottomMargin()
        nextFab.setBottomMargin()
    }

    override fun setListeners() = with(binding) {
        with(topAppBar) {
            setOnMenuItemClickListener {
                onMenuClick(it.itemId)

            }

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        nextFab.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_new_route_map_to_newRoutePhotosFragment)
        }
    }

    private fun onMenuClick(itemId: Int): Boolean {
        when (itemId) {
            R.id.search_place -> {
                val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                launcherPlaces.launch(fields)
            }
            R.id.import_gpx -> {
                launcherGpx.launch(Unit)
            }
            R.id.clear_map -> {
                viewModel.onEvent(CreateRouteEvent.CleanStop)
            }
        }
        return true
    }

    override fun setMapListeners() = with(viewModel) {
        map.setOnMapClickListener {
            onEvent(CreateRouteEvent.AddedStop(it.latitude, it.longitude))
            //getDirections()
        }
    }

    override fun setFirstCameraPosition() = with(viewModel.uiStateMap.value.stops.firstOrNull()) {
        val position = this?.let {
            LatLng(it.latitude, it.longitude)
        } ?: LatLng(40.82806233458257, 14.19321142133755)

        map.moveAndZoomCamera(position)
    }

    override fun collectState() = with(viewModel) {
        collectOnLifecycleScope(uiStateMap) {
            map.clear()

            if (it.stops.isNotEmpty()) {
                it.stops.map { stop ->
                    map.addCustomMarker(
                        stop.stopNumber.toString(),
                        LatLng(stop.latitude, stop.longitude)
                    )
                }

                map.addPolyline(it.polylineOptions)

                if (it.isLoadedFromGPX) {
                    setFirstCameraPosition()
                }

                binding.nextFab.isEnabled = it.isButtonEnabled
            }
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
                else -> {}
            }
        }
    }

    private fun setupGpx() {
        launcherGpx = registerForActivityResult(GpxPickerContract()) { result ->
            result?.let {
                viewModel.onEvent(CreateRouteEvent.InsertedGpx(it))
            }
        }
    }
}