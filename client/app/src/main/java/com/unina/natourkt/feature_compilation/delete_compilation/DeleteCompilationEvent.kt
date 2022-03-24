package com.unina.natourkt.feature_compilation.delete_compilation

sealed class DeleteCompilationEvent {
    object OnDelete : DeleteCompilationEvent()
}