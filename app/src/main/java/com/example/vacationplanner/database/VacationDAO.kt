package com.example.vacationplanner.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface VacationDao {
    @Query("select * from vacations")
    fun getVacations(): LiveData<List<DatabaseVacationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVacation(vacationEntity: DatabaseVacationEntity)

    @Query("UPDATE vacations SET imageBLOB = (CASE WHEN imageBLOB IS NULL THEN :blob ELSE imageBLOB END) WHERE cityName = :cityName")
    fun updateImage (cityName : String, blob : String)
}

@Database(entities = [DatabaseVacationEntity::class], version = 1
)
abstract class VacationDatabase: RoomDatabase() {
    abstract fun vacationDao(): VacationDao
    companion object {
        @Volatile
        private var INSTANCE: VacationDatabase? = null
        fun getDatabase(
            context: Context,
        ): VacationDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VacationDatabase::class.java,
                    "vacation"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

