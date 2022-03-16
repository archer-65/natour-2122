package com.unina.natourkt.feature_compilation.create_compilation

import android.net.Uri

sealed class CreateCompilationEvent {
    // GENERAL
    object Upload : CreateCompilationEvent()

    // INFO
    data class EnteredTitle(val title: String) : CreateCompilationEvent()
    data class EnteredDescription(val description: String) : CreateCompilationEvent()

    // PHOTO
    data class InsertedPhoto(val photo: Uri) : CreateCompilationEvent()
}