package com.example.vacationplanner.repository

import androidx.annotation.WorkerThread
import com.example.vacationplanner.database.VacationDao
import com.example.vacationplanner.database.VacationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VacationRepository(private val vacationDao: VacationDao) {

    val allVacations : Flow<List<VacationEntity>> = vacationDao.getVacations()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertVacation(vacationEntity: VacationEntity) {
        withContext(Dispatchers.IO) {
            vacationDao.insertVacation(vacationEntity)
        }
    }

}