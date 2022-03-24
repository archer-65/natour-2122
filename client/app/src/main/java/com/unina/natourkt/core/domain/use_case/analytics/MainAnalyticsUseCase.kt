package com.unina.natourkt.core.domain.use_case.analytics

import com.unina.natourkt.R
import com.unina.natourkt.core.analytics.AnalyticsSender
import com.unina.natourkt.core.analytics.AppEvents
import javax.inject.Inject

class MainAnalyticsUseCase @Inject constructor(
    private val eventSender: AnalyticsSender
) {

    private var prevDestination: Int? = null

    fun destinationChanged(destinationId: Int) {
        if (destinationId == prevDestination) return

        prevDestination?.let {
            eventSender.sendEvent(
                AppEvents.Navigated(
                    from = getNavigationDestination(it),
                    to = getNavigationDestination(destinationId)
                )
            )
        }
        prevDestination = destinationId
    }

    fun sessionStarted() {
        eventSender.sendEvent(AppEvents.SessionStart)
    }

    private fun getNavigationDestination(destination: Int): AppEvents.Navigated.NavigationSource =
        when (destination) {
            R.id.navigation_home -> AppEvents.Navigated.NavigationSource.HOME
            R.id.navigation_routes -> AppEvents.Navigated.NavigationSource.ROUTES
            R.id.navigation_profile -> AppEvents.Navigated.NavigationSource.PROFILE
            R.id.navigation_chat_list -> AppEvents.Navigated.NavigationSource.CHATS
            R.id.navigation_admin_board -> AppEvents.Navigated.NavigationSource.ADMIN
            R.id.reportDetailsFragment -> AppEvents.Navigated.NavigationSource.REPORT_DETAIL
            R.id.navigation_login -> AppEvents.Navigated.NavigationSource.LOGIN
            R.id.navigation_forgot_password -> AppEvents.Navigated.NavigationSource.FORGOT_PASSWORD
            R.id.navigation_registration -> AppEvents.Navigated.NavigationSource.SIGNUP
            R.id.settingsFragment -> AppEvents.Navigated.NavigationSource.LOGOUT
            R.id.chatFragment -> AppEvents.Navigated.NavigationSource.CHAT
            R.id.chatSearchFragment -> AppEvents.Navigated.NavigationSource.USER_SEARCH
            R.id.navigation_compilation_details -> AppEvents.Navigated.NavigationSource.COMPILATION_DETAIL
            R.id.navigation_create_compilation -> AppEvents.Navigated.NavigationSource.CREATE_COMPILATION
            R.id.deleteCompilationDialog -> AppEvents.Navigated.NavigationSource.DELETE_COMPILATION
            R.id.saveIntoCompilationDialog -> AppEvents.Navigated.NavigationSource.SAVE_TO_COMPILATION

            R.id.navigation_create_post -> AppEvents.Navigated.NavigationSource.CREATE_POST
            R.id.deletePostDialog -> AppEvents.Navigated.NavigationSource.DELETE_POST
            R.id.navigation_post_details -> AppEvents.Navigated.NavigationSource.POST_DETAIL
            R.id.reportPostDialog -> AppEvents.Navigated.NavigationSource.REPORT_POST
            R.id.navigation_create_route_flow -> AppEvents.Navigated.NavigationSource.CREATE_POST
            R.id.deleteRouteDialog3 -> AppEvents.Navigated.NavigationSource.DELETE_ROUTE
            R.id.rateRouteDialogFragment2 -> AppEvents.Navigated.NavigationSource.RATE_ROUTE
            R.id.navigation_dialog_report_route -> AppEvents.Navigated.NavigationSource.REPORT_ROUTE
            R.id.navigation_route_details -> AppEvents.Navigated.NavigationSource.ROUTE_DETAIL
            R.id.navigation_search_route -> AppEvents.Navigated.NavigationSource.ROUTE_SEARCH
            R.id.updateRouteFullDialog2 -> AppEvents.Navigated.NavigationSource.UPDATE_ROUTE

            else -> AppEvents.Navigated.NavigationSource.HOME
        }
}