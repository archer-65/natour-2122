package com.unina.natourkt.core.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteStopUi(
    val stopNumber: Int,
    val latitude: Double,
    val longitude: Double,
): Parcelable
