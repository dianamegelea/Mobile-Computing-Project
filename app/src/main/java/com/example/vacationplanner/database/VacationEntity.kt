package com.example.vacationplanner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vacationplanner.model.VacationData


/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */


/**
 * DatabaseVideo represents a video entity in the database.
 */
@Entity(tableName = "vacations")
data class DatabaseVacationEntity (
     val cityName: String,
     val startDate: String,
     val noDays: Int) {

    @PrimaryKey(autoGenerate = true)
    var vacationId: Int = 0

    @ColumnInfo(name="searchURL")
    var searchURL: String? = null

    @ColumnInfo(name="imageBLOB")
    var imageBLOB: String? = null
}

/**
 * Map DatabaseVacations to domain entities
 */
fun List<DatabaseVacationEntity>.asDomainModel(): List<VacationData> {
    return map {
        VacationData(
            cityName = it.cityName,
            startDate = it.startDate,
            searchURL = it.searchURL,
            imageBlob = it.imageBLOB,
            noDays = it.noDays
        )
    }
}