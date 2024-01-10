
package com.example.vacationplanner.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vacationplanner.converters.Converters
import com.example.vacationplanner.database.VacationDatabase
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.repository.ImageRepository
import com.example.vacationplanner.repository.VacationRepository
import com.example.vacationplanner.utils.Utils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class VacationViewModel(private val application: Application) : ViewModel() {

    private val vacationRepository = VacationRepository(VacationDatabase.getDatabase(application))
    private val imageRepository = ImageRepository()

    val allVacations: LiveData<List<VacationData>> = vacationRepository.allVacations

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?>
        get() = _error

    private val _navigate = MutableLiveData<VacationData?>(null)
    val navigate: LiveData<VacationData?>
        get() = _navigate

    fun onNavigated() {
        _navigate.value = null
    }

    fun onDisplayedError() {
        _error.value = null
    }


    fun insert(vacationEntity: VacationData) = viewModelScope.launch {
        vacationRepository.insertVacation(vacationEntity)
    }

    fun onItemClicked(data: VacationData) {
        val start = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(data.startDate)

        // the API only gives the forecast for 5 days on free version; this is what I'm checking here
        if (!Utils.isOnline(application.applicationContext)) {
            _error.value = "Please go online to check the weather"
        } else if (!Utils.isDateWithinRange(start!!)) {
            _error.value = "Please wait until a closer date"
        } else {
            _navigate.value = data
        }
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