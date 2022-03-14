package com.unina.natourkt.core.presentation.model.mapper

interface UiMapper<D, U> {

    fun mapToUi(domainEntity: D): U
}