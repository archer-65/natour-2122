package com.unina.natourkt.feature_compilation.save_into_compilation

sealed class SaveIntoCompilationEvent {
    data class OnSave(val compilationId: Long) : SaveIntoCompilationEvent()
}