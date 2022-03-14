package com.unina.natourkt.core.domain.model

import com.google.android.gms.maps.model.LatLng

data class DirectionsPolyline(
    val points: List<LatLng>
)