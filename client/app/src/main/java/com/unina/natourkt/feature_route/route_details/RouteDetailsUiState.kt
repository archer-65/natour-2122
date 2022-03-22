package com.unina.natourkt.feature_route.route_details

import com.google.android.gms.maps.model.PolylineOptions
import com.unina.natourkt.R
import com.unina.natourkt.core.presentation.model.RouteDetailsUi
import com.unina.natourkt.core.presentation.model.UserUi
import com.unina.natourkt.core.presentation.util.UiText
import com.unina.natourkt.core.presentation.util.formatFull
import com.unina.natourkt.core.util.Difficulty

data class RouteDetailsUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val loggedUser: UserUi? = null,
    val route: RouteDetailsUi? = null,
    val polylineOptions: PolylineOptions = PolylineOptions()
) {
    val menu: Int?
        get() {
            return if (loggedUser != null && route?.author?.id != null) {
                if (loggedUser.isAdmin) {
                    return R.menu.top_bar_admin_route_menu
                } else if (loggedUser.id == route.author.id) {
                    return R.menu.top_bar_owner_route_menu
                } else {
                    return R.menu.top_bar_viewer_route_menu
                }
            } else {
                null
            }
        }

    val canRateRoute: Boolean
        get() = loggedUser?.id != route?.author?.id

    val canContactAuthor: Boolean
        get() = loggedUser?.id != route?.author?.id

    val isWarningPresent: Boolean
        get() = route?.modifiedDate != null || route?.isReported == true

    val warningText: UiText?
        get() {
            return if (route?.modifiedDate == null && route?.isReported == true) {
                UiText.StringResource(R.string.warning_not_updated_or_incorrect)
            } else if (route?.modifiedDate != null) {
                UiText.DynamicString(route.modifiedDate.formatFull())
            } else {
                null
            }
        }

    val difficultyText: UiText?
        get() {
            return when (route?.difficulty) {
                Difficulty.EASY -> UiText.StringResource(R.string.easy_label)
                Difficulty.MEDIUM -> UiText.StringResource(R.string.medium_label)
                Difficulty.HARD -> UiText.StringResource(R.string.hard_label)
                else -> null
            }
        }
}