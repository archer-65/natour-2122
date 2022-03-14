package com.unina.natourkt.core.data.remote.dto.mapper

interface ApiMapper<E, D> {

    fun mapToDomain(apiEntity: E): D
}