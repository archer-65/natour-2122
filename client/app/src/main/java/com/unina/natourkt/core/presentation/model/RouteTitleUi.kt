package com.unina.natourkt.core.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteTitleUi(
    val routeId: Long,
    val routeTitle: String,
) : Parcelable