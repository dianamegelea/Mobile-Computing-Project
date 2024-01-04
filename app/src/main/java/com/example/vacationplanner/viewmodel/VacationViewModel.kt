package com.example.vacationplanner.viewmodel;

import androidx.lifecycle.LiveData
import com.example.vacationplanner.database.VacationEntity
import com.example.vacationplanner.repository.VacationRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VacationViewModel(private val repository: VacationRepository) : ViewModel() {

    val allVacations: LiveData<List<VacationEntity>> = repository.allVacations.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(vacationEntity: VacationEntity) = viewModelScope.launch {
        repository.insertVacation(vacationEntity)
    }
}

class VacationViewModelFactory(private val repository: VacationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VacationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VacationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
