package com.example.weathertrackingapp.data.mappers

interface Mapper<DTO, Domain> {

    fun dtoToDomain(input: DTO): Domain
}