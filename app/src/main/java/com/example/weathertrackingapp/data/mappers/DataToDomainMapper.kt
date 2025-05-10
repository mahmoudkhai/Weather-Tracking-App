package com.example.weathertrackingapp.data.mappers

/**
 * Interface for mapping data transfer objects (DTO) to domain entities.
 *
 * @param DTO The type of the data transfer object.
 * @param Entity The type of the domain entity.
 */
interface DataToDomainMapper<DTO, Entity> {
    fun dtoToEntity(input: DTO): Entity
}