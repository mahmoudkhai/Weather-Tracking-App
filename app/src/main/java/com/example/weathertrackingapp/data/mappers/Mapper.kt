package com.example.weathertrackingapp.data.mappers

interface Mapper<DTO, Entity> {

    fun dtoToDomain(input: DTO): Entity
}