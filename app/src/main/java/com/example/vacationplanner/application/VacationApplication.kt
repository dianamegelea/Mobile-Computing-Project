package com.example.vacationplanner.application

import android.app.Application
import com.example.vacationplanner.database.VacationDatabase
import com.example.vacationplanner.repository.VacationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob



class VacationApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { VacationDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { VacationRepository(database.vacationDao()) }
}