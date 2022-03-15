package com.unina.natourkt.feature_route.new_route

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRouteMapBinding
import com.unina.natourkt.core.presentation.contract.GpxPickerContract
import com.unina.natourkt.core.presentation.base.fragment.BaseMapFragment
import com.unina.natourkt.core.presentation.util.addCustomMarker
import com.unina.natourkt.core.presentation.util.moveAndZoomCamera
import com.unina.natourkt.core.presentation.util.setBottomMargin
import com.unina.natourkt.core.presentation.util.setTopMargin

class NewRouteMapFragment :
    BaseMapFragment<FragmentNewRouteMapBinding, NewRouteViewModel, MapView>() {

    private lateinit var launcherGpx: ActivityResultLauncher<Unit>

    private val viewModel: NewRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

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
                viewModel.cleanStops()
            }
        }
        return true
    }

    override fun setMapListeners() = with(viewModel) {
        map.setOnMapClickListener {
            addStop(it.latitude, it.longitude)
            getDirections()
        }
    }

    override fun setFirstCameraPosition() = with(viewModel.uiState.value.routeStops.firstOrNull()) {
        val position = this?.let {
            LatLng(it.latitude, it.longitude)
        } ?: LatLng(40.82806233458257, 14.19321142133755)

        map.moveAndZoomCamera(position)
    }

    override fun collectState() = with(viewModel) {
        collectOnLifecycleScope(uiState) {
            map.clear()

            if (it.routeStops.isNotEmpty()) {
                it.routeStops.map { stop ->
                    map.addCustomMarker(
                        stop.stopNumber.toString(),
                        LatLng(stop.latitude, stop.longitude)
                    )
                }

                if (it.routeStops.size >= 2) {
                    map.addPolyline(it.polylineOptions)
                }

                if (it.isLoadedFromGPX) {
                    setFirstCameraPosition()
                }

                binding.nextFab.isEnabled = it.routeStops.size >= 2
            }
        }
    }

    private fun setupGpx() {
        launcherGpx = registerForActivityResult(GpxPickerContract()) { result ->
            result?.let {
                viewModel.setFromGpx(it)
            }
        }
    }
}