package com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Abstract class that implements the CSVFileHelper interface to handle CSV file operations.
 * It provides functionality to insert, read, and override data in a CSV file.
 *
 * @param DTO The data type being stored in or read from the CSV file.
 * @param filePath The path of the CSV file.
 * @param headers The list of headers for the CSV file.
 */
abstract class CsvFileHelperImpl<DTO>(
    filePath: String,
    private val headers: List<String>,
) : CSVFileHelper<DTO> {

    private val file: File = File(filePath)

    init {
        if (!file.exists()) {
            FileWriter(file, true).use { writer ->
//                writer.appendLine(headers.joinToString(","))
            }
        }
    }

    override fun insertNewData(dto: DTO) {
        val newRow = fromDtoToCsvRow(dto)
        BufferedWriter(FileWriter(file, KEEP_ALL_DATA)).use { writer ->
            writer.appendLine(newRow)
        }
    }

    override fun overrideAllOldData(dto: DTO) {
        val newRaw = fromDtoToCsvRow(dto).also {
            Log.d(TAG, "new raw data to be written in the file = $it ")
        }
        BufferedWriter(FileWriter(file, OVERRIDE_ALL_DATA)).use { writer ->
            writer.appendLine(newRaw)
        }
        Log.d(TAG, "Verifying written file content: ${file.readLines().last()}")
    }

    override fun readMoseRecentData(): DTO = fromCsvRowToDto(file.readLines().last())

    abstract fun fromDtoToCsvRow(dto: DTO): String

    abstract fun fromCsvRowToDto(row: String): DTO


    companion object {
        const val KEEP_ALL_DATA = true
        const val OVERRIDE_ALL_DATA = false
    }
}