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
        Log.d(TAG, "overrideAllOldData of csv file with this Dto: $dto ")
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
        file.readLines().asSequence().also {
            Log.d(TAG, "current file with before dropping first row = : ${it.toString()} ")
        }.also {
            Log.d(TAG, "current file with after dropping first row = : ${it.toString()} ")
        }.filter { it.isNotBlank() }.also {
            Log.d(TAG, "current file with after filtering not blank values = : $it ")
        }.map(::fromCsvRowToDto).toList().also {
            Log.d(TAG, "final cached dto after reading the file = : $it ")
        }

    abstract fun fromDtoToCsvRow(dto: DTO): String

    abstract fun fromCsvRowToDto(row: String): DTO

    private fun <T> safeCsvFileCall(function: () -> T): T {
        return try {
            function()
        } catch (e: IOException) {
            throw CustomException.DataException.LocalInputOutputException
        } catch (e: IndexOutOfBoundsException) {
            throw CustomException.DataException.NoCachedDataFound
        } catch (e: Exception) {
            Log.e(TAG, "Error while reading/writing CSV file: ${e.toString()}")
            throw CustomException.DataException.UnKnownDataException(e)
        }
    }

    companion object {
        const val FIRST_ROW_OF_CSV_FILE = 1
        const val KEEP_ALL_DATA = true
        const val OVERRIDE_ALL_DATA = false
    }
}