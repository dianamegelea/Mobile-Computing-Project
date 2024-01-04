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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Dao
interface VacationDao {
    @Query("select * from vacations")
    fun getVacations(): Flow<List<VacationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVacation(vacationEntity: VacationEntity)
}

@Database(entities = [VacationEntity::class], version = 1)
abstract class VacationDatabase: RoomDatabase() {
    abstract fun vacationDao(): VacationDao
    companion object {
        @Volatile
        private var INSTANCE: VacationDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
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

