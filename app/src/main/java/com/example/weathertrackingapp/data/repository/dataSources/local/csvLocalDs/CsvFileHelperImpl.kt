package com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs

import com.example.weathertrackingapp.common.customException.CustomException
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

abstract class CsvFileHelperImpl<DTO>(
    filePath: String,
    private val headers: List<String>,
) : CSVFileHelper<DTO> {

    private val file: File = File(filePath)

    init {
        if (!file.exists()) {
            FileWriter(file, true).use { writer ->
                writer.appendLine(headers.joinToString(","))
            }
        }
    }

    override fun insertNewData(dto: DTO) = safeCsvFileCall<Unit> {
        val newRow = fromDtoToCsvRow(dto)
        BufferedWriter(FileWriter(file, KEEP_ALL_DATA)).use { writer ->
            writer.appendLine(newRow)
        }
    }

    override fun overrideAllOldData(dto: DTO) {
        val newRaw = fromDtoToCsvRow(dto)
        BufferedWriter(FileWriter(file, OVERRIDE_ALL_DATA)).use { writer ->
            writer.appendLine(newRaw)
        }
    }

    override fun readAllDTOs(): List<DTO> = safeCsvFileCall(::getListOfDtoObjectFromCsvFile)

    override fun readMoseRecentData(): DTO = safeCsvFileCall {
        getListOfDtoObjectFromCsvFile().last()
    }

    private fun getListOfDtoObjectFromCsvFile() =
        file.readLines().asSequence().drop(FIRST_ROW_OF_CSV_FILE).filter { it.isNotBlank() }
            .map(::fromCsvRowToDto).toList()

    abstract fun fromDtoToCsvRow(dto: DTO): String

    abstract fun fromCsvRowToDto(row: String): DTO

    private fun <T> safeCsvFileCall(function: () -> T): T {
        return try {
            function()
        } catch (e: IOException) {
            throw CustomException.DataException.LocalInputOutputException
        } catch (e: Exception) {
            throw CustomException.DataException.UnKnownDataException
        }
    }

    companion object {
        const val FIRST_ROW_OF_CSV_FILE = 1
        const val KEEP_ALL_DATA = true
        const val OVERRIDE_ALL_DATA = false
    }
}