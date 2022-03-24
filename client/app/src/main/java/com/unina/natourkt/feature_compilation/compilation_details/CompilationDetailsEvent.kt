package com.unina.natourkt.feature_compilation.compilation_details

sealed class CompilationDetailsEvent {
    data class OnDeleteRouteFromCompilation(val routeId: Long, val position: Int) : CompilationDetailsEvent()
}