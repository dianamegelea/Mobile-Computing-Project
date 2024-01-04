package com.example.vacationplanner.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacations")
data class VacationEntity(
    var name: String,
    var date: String,
    var days: Int
){
    @PrimaryKey(autoGenerate = true)
    var vacationId: Int = 0
}

//data class DatabaseVideo constructor(
//    val url: String,
//    val updated: String,
//    val title: String,
//    val description: String,
//    val thumbnail: String)