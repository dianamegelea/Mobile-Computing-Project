package com.example.vacationplanner.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.vacationplanner.database.DatabaseVacationEntity
import com.example.vacationplanner.database.VacationDatabase
import com.example.vacationplanner.database.asDomainModel
import com.example.vacationplanner.model.VacationData
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext

class VacationRepository(private val database: VacationDatabase) {

    val allVacations : LiveData<List<VacationData>> = database.vacationDao().getVacations().map {
        it.asDomainModel()
    }

    @WorkerThread
    suspend fun updateVacationImage(cityName : String, imageBlob : String) {
        database.vacationDao().updateImage(cityName, imageBlob)
    }

    @WorkerThread
    suspend fun updateSearchURL(cityName : String, urlString : String) {
        database.vacationDao().updateSearchURL(cityName, urlString)
    }

    @WorkerThread
        suspend fun insertVacation(vacationEntity: VacationData) {
        database.vacationDao().insertVacation(DatabaseVacationEntity(
            vacationEntity.cityName,
            vacationEntity.startDate,
            vacationEntity.noDays
        ))
    }
}