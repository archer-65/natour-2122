package com.unina.natourkt.feature_admin.admin_board

import com.unina.natourkt.core.presentation.model.UserUi

data class AdminBoardUiState(
    val isLoading: Boolean = false,
    val loggedUser: UserUi? = null,
)