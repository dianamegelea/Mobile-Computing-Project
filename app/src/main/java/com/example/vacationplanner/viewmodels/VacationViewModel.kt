
package com.example.vacationplanner.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vacationplanner.converters.Converters
import com.example.vacationplanner.database.VacationDatabase
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.repository.ImageRepository
import com.example.vacationplanner.repository.VacationRepository
import kotlinx.coroutines.launch

class VacationViewModel(application: Application) : ViewModel() {

    private val vacationRepository = VacationRepository(VacationDatabase.getDatabase(application))
    private val imageRepository = ImageRepository()

    val allVacations: LiveData<List<VacationData>> = vacationRepository.allVacations

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(vacationEntity: VacationData) = viewModelScope.launch {
        vacationRepository.insertVacation(vacationEntity)
    }

    fun updateImage(cityName: String, drawable: Drawable) = viewModelScope.launch {
        val bitmap = Bitmap.createScaledBitmap((drawable as BitmapDrawable).bitmap, 120, 120, false)
        val bitmapBase64Encoded = Converters.BitmapToBase64(bitmap)
        vacationRepository.updateVacationImage(cityName, bitmapBase64Encoded!!)
    }

    fun downloadLink(cityName : String)  = viewModelScope.launch {
        try {
            val response = imageRepository.downloadBestImage(cityName)
            val items = response.items
            if (items.isNotEmpty()) {
                vacationRepository.updateSearchURL(cityName, items[0].link)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class VacationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VacationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VacationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}