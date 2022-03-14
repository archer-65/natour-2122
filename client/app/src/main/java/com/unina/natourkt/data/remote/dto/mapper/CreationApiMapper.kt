package com.unina.natourkt.data.remote.dto.mapper

interface CreationApiMapper<D, E> {

    fun mapToDto(domainEntity: D): E
}