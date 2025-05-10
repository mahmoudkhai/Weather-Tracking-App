package com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs

interface CSVFileHelper<DTO> {
    fun insertNewData(dto: DTO)
    fun readMoseRecentData(): DTO
    fun overrideAllOldData(dto: DTO)
}