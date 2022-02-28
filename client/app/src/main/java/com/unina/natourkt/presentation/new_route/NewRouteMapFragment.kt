package com.unina.natourkt.presentation.new_route

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.R
import com.unina.natourkt.databinding.FragmentNewRouteMapBinding
import com.unina.natourkt.presentation.base.contract.PlacesContract
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewRouteMapFragment : Fragment(), OnMapReadyCallback {

    // This property is only valid between OnCreateView and
    // onDestroyView.
    private var _binding: FragmentNewRouteMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var launcherPlaces: ActivityResultLauncher<List<Place.Field>>
    private lateinit var map: GoogleMap

    private val newRouteViewModel: NewRouteViewModel by hiltNavGraphViewModels(R.id.navigation_new_route_flow)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewRouteMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // We are not using MapFragment anymore, it's useless and negative for performance.
        // Implementing MapView instead of Fragment requires lifecycle management.
        // Here we are creating our MapView and calling onMapReady callback.
        binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync {
                map = it
                onMapReady(map)
            }
        }
        initPlacesSearch()

        setupUi()
        setListeners()
    }

    /**
     * Function to initialize [launcherPlaces] with [registerForActivityResult], replacing
     * [startActivityForResult] and [onActivityResult] (deprecated)
     * @see [PlacesContract]
     */
    private fun initPlacesSearch() {
        launcherPlaces = registerForActivityResult(PlacesContract()) { result ->
            result?.let {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(it.latLng!!, 15f))
            }
        }
    }

    /**
     * Basic settings for UI
     */
    private fun setupUi() = with(binding) {
        mapView.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        nextFab.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }

        topAppBar.apply {
            applyInsetter {
                type(statusBars = true) {
                    margin()
                }
            }
        }
    }

    private fun setListeners() = with(binding) {
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search_place -> {
                    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                    launcherPlaces.launch(fields)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) = with(newRouteViewModel) {
        initMap()
        collectState()

        googleMap.setOnMapClickListener {
            addStop(it.latitude, it.longitude)
            Log.i("AGGIUNTA STOP", it.toString())
        }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newRouteViewModel.uiState.collect { uiState ->
                    uiState.apply {
                        if (routeStops.isNotEmpty()) {
                            bindStops(routeStops)
                        }
                    }
                }
            }
        }
    }

    private fun bindStops(stops: List<NewRouteStop>) {
        stops.map { stop ->
            val position = LatLng(stop.latitude, stop.longitude)

            Log.i("Aggiunta marker", position.toString())
            map.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("${stop.stopNumber}")
                    .draggable(true)
            )
        }
    }

    private fun initMap() {
        val firstStop = newRouteViewModel.uiState.value.routeStops.firstOrNull()

        val position = if (firstStop != null) {
            LatLng(firstStop.latitude, firstStop.longitude)
        } else {
            LatLng(40.82806233458257, 14.19321142133755)
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
    }

    // Here we are overriding lifecycle functions to manage MapView's lifecycle
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
    // End of lifecycle management for MapView
}