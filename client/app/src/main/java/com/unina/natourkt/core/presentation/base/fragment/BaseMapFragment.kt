package com.unina.natourkt.core.presentation.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.libraries.places.api.model.Place
import com.unina.natourkt.core.presentation.contract.PlacesContract

abstract class BaseMapFragment<VB : ViewBinding, VM : ViewModel, MapBinding : MapView> :
    Fragment(), OnMapReadyCallback {

    /**
     * Activity result launcher for `Places API`
     */
    protected lateinit var launcherPlaces: ActivityResultLauncher<List<Place.Field>>

    /**
     * This property serves as ViewModel generalization
     */
    protected lateinit var baseViewModel: VM
    protected abstract fun getVM(): VM

    /**
     * This property is valid only until onDestroyView is called
     */
    protected var _binding: VB? = null
    protected val binding get() = _binding!!
    protected abstract fun getViewBinding(): VB

    /**
     * This property serves as Map base attribute
     */
    protected lateinit var map: GoogleMap
    protected lateinit var mapView: MapBinding
    protected abstract fun getMapBinding(): MapBinding

    /**
     * Overrides `onCreateView` only to return binding's root view, initialized in [init]
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        return binding.root
    }

    /**
     * Overrides `onViewCreated`, here all the useful basic functions are called
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupMap(savedInstanceState)
        initPlacesSearch()
        setListeners()
    }

    /**
     * Manages the `onResume` behavior of MapView
     */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    /**
     * Manages the `onStart` behavior of MapView
     */
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    /**
     * Manages the `onStop` behavior of MapView
     */
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    /**
     * Manages the `onPause` behavior of MapView
     */
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    /**
     * Manages the `onLowMemory` behavior of MapView
     */
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    /**
     * Manages the `onSaveInstanceState` behavior of MapView
     * DON'T WORK!!!
     */
//    override fun onSaveInstanceState(outState: Bundle) {dd
//        super.onSaveInstanceState(outState)
//        mapView.onSaveInstanceState(outState)
//    }

    /**
     * [_binding] is set to null. This because Fragments could outlive their views, so we clean up
     * every reference to the [ViewBinding] class.
     * Manages [mapView] destruction.
     */
    override fun onDestroyView() {
        mapView.onDestroy()
        super.onDestroyView()
        _binding = null
    }

    /**
     * It instantiate a binding object for the view, and initializes the view model
     */
    open fun init() {
        _binding = getViewBinding()
        baseViewModel = getVM()
        mapView = getMapBinding()
    }

    /**
     * This function setups every View
     */
    open fun setupUi() {}

    open fun setupMap(savedInstanceState: Bundle?) = mapView.apply {
        onCreate(savedInstanceState)
        getMapAsync {
            map = it
            onMapReady(map)
        }
    }

    /**
     * Function to initialize [launcherPlaces] with [registerForActivityResult], replacing
     * [startActivityForResult] and [onActivityResult] (deprecated)
     * @see [PlacesContract]
     */
    open fun initPlacesSearch() {
        launcherPlaces = registerForActivityResult(PlacesContract()) { result ->
            result?.let {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(it.latLng!!, 15f))
            }
        }
    }

    /**
     * This function sets any kind of listener
     */
    open fun setListeners() {}


    /**
     * This function sets map related listeners
     */
    open fun setMapListeners() {}


    /**
     * This function sets any kind of text listener
     */
    open fun setTextChangedListeners() {}

    /**
     * This function serves as a way to set the initial map position.
     * Usually the initial position is the first stop of a route or a placeholder chosen
     * by developers :)
     */
    open fun setFirstCameraPosition() {}

    /**
     * This function serves as a way to collect states from ViewModel
     */
    open fun collectState() {}

    /**
     * Overriding function of interface for when the map is ready to be used.
     * Once an instance of this interface is set on a MapFragment or MapView object,
     * the onMapReady(GoogleMap) method is triggered when the map is ready to be used and provides a non-null instance of GoogleMap.
     * If Google Play services is not installed on the device, the user will be prompted to install it,
     * and the onMapReady(GoogleMap) method will only be triggered when the user has installed it and returned to the app.
     */
    override fun onMapReady(map: GoogleMap) {
        map.setPadding(0, 0, 0, 150)
        setFirstCameraPosition()
        collectState()
        setMapListeners()
    }
}