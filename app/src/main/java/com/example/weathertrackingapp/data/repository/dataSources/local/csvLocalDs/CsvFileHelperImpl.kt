package com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
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

    override fun readMoseRecentData(): DTO = fromCsvRowToDto(file.readLines().last().also {
        Log.d(TAG, "this is the string to convert to dto $it")
    })


//        file.readLines().also {
//            Log.d(TAG, "current file with before dropping first row = : ${it.toString()} ")
//        }fromCsvRowToDto).also {
//            Log.d(TAG, "final cached dto after reading the file = : $it ")
//        }

    abstract fun fromDtoToCsvRow(dto: DTO): String

    abstract fun fromCsvRowToDto(row: String): DTO


    companion object {
        const val FIRST_ROW_OF_CSV_FILE = 1
        const val KEEP_ALL_DATA = true
        const val OVERRIDE_ALL_DATA = false
    }
}