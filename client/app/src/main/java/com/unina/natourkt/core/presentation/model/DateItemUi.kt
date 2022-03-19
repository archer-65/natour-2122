package com.unina.natourkt.core.presentation.model

import java.time.LocalDate

data class DateItemUi(
    val date: LocalDate,
    override val type: MessageType = MessageType.TYPE_DATE
) : ChatGenericUi