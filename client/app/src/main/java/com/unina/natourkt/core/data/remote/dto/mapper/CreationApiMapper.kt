package com.unina.natourkt.core.data.remote.dto.mapper

interface CreationApiMapper<D, E> {

    fun mapToDto(domainEntity: D): E
}