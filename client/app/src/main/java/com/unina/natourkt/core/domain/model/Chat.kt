package com.unina.natourkt.core.domain.model

import java.time.LocalDate

data class Chat(
    val id: Long,
    val creationDate: LocalDate,
    val firstMember: User,
    val secondMember: User,
)