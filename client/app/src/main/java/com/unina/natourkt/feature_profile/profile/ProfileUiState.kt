package com.unina.natourkt.feature_profile.profile

import com.unina.natourkt.core.presentation.model.UserUi

data class ProfileUiState(
    val loggedUser: UserUi? = null,
    val isLoading: Boolean = false,
    val isPhotoUpdated: Boolean = false,

)