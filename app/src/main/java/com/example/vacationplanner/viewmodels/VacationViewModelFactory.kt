
package com.example.vacationplanner.viewmodel;

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.vacationplanner.repository.VacationRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.vacationplanner.database.VacationDatabase
import com.example.vacationplanner.model.VacationData
import kotlinx.coroutines.launch

class VacationViewModel(val repository: VacationRepository) : ViewModel() {

    val allVacations: LiveData<List<VacationData>> = repository.allVacations

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(vacationEntity: VacationData) = viewModelScope.launch {
        repository.insertVacation(vacationEntity)
    }
}

class VacationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VacationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VacationViewModel(VacationRepository(VacationDatabase.getDatabase(application))) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}