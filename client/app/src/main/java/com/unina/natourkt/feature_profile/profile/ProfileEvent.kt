package com.unina.natourkt.feature_profile.profile

import android.net.Uri

sealed class ProfileEvent {
    data class OnUpdatePhoto(val uri: Uri) : ProfileEvent()
}