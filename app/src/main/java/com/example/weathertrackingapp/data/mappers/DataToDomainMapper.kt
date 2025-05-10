package com.example.weathertrackingapp.data.mappers

interface DataToDomainMapper<DTO, Entity> {
    fun dtoToEntity(input: DTO): Entity
}