package com.unina.natourkt.data.remote.dto.mapper

interface ApiMapper<E, D> {

    fun mapToDomain(apiEntity: E): D
}