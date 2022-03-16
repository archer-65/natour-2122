package com.unina.natourkt.core.domain.model

data class CompilationCreation(
    val title: String,
    val description: String,
    val photo: String,
    val author: User?
)