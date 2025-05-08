package com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs

interface CSVFileHelper<DTO> {
    fun writeDtoAsString(dto: DTO)
    fun readAllDTOs(): List<DTO>
    fun readMoseRecentDto(): DTO
}