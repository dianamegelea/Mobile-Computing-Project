package com.example.vacationplanner

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.view.VacationAdapter
import com.example.vacationplanner.viewmodels.VacationViewModel
import com.example.vacationplanner.viewmodels.VacationViewModelFactory
import com.example.vacationplanner.viewmodels.AppViewModel
import com.example.vacationplanner.viewmodels.AppViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var addButton : FloatingActionButton
    private lateinit var recView : RecyclerView

    private lateinit var appViewModel: AppViewModel
    private lateinit var vacationViewModel: VacationViewModel

    private var vacationList: MutableList<VacationData> = mutableListOf()

    private lateinit var vacationAdapter: VacationAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vacationViewModel = ViewModelProvider(this, VacationViewModelFactory(this.application))[VacationViewModel::class.java]
        appViewModel = ViewModelProvider(this, AppViewModelFactory(this.application))[AppViewModel::class.java]

        vacationAdapter = appViewModel.getVacationAdapter()

        vacationViewModel.allVacations.observe(this) { vacation ->
            // Update the cached copy of the words in the adapter.
            vacation.let {
                vacationAdapter.vacationList = it.toMutableList()
                vacationAdapter.notifyDataSetChanged()
            }
        }

        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                }
            }
            return false
        }


        addButton = findViewById(R.id.floatingButton)
        recView = findViewById(R.id.recycleview)

        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = vacationAdapter

        addButton.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val v = inflater.inflate(R.layout.add_item, null)

            val cityName = v.findViewById<EditText>(R.id.citynameEditText)
            val startDate = v.findViewById<EditText>(R.id.startdateEditText)
            val nrDays = v.findViewById<EditText>(R.id.nrofdaysEditText)

            val addDialog = AlertDialog.Builder(this)
            addDialog.setView(v)

            startDate.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this,
                    { _, pickerYear, monthOfYear, dayOfMonth ->
                        val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + pickerYear)
                        startDate.setText(dat)
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }

            addDialog.setPositiveButton("Ok") {
                    dialog,_ ->
                val name = cityName.text.toString()
                val date = startDate.text.toString()
                val days = nrDays.text.toString().toInt()
                val vacationData = VacationData(name, date, null, days)

                vacationViewModel.insert(vacationData)
                vacationList.add(vacationData)
                vacationAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Adding vacation...", Toast.LENGTH_SHORT).show()
                dialog.dismiss()


            }

            addDialog.setNegativeButton("Cancel") {
                    dialog,_ ->
                dialog.dismiss()
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }
            addDialog.create()
            addDialog.show()
        }

        vacationAdapter.onItemClick = fun(it: VacationData) {
            val start = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it.startDate)

            // the API only gives the forecast for 5 days on free version; this is what I'm checking here
            if (!isOnline(application.applicationContext)) {
                Toast.makeText(
                    this,
                    "Please go online to check the weather",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!isDateWithinRange(start!!)) {
                Toast.makeText(
                    this,
                    "Wait until you get closer to the start date of your vacation",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, WeatherForecastForCity::class.java)
                intent.putExtra("vacationdata", it)
                startActivity(intent)
            }

        }
    }

    private fun isDateWithinRange(startDate: Date): Boolean {
        val currentDate = Calendar.getInstance().time
        val endDate = calculateDateAfterNDays(currentDate, 5)
        return startDate <= endDate
    }

    private fun calculateDateAfterNDays(date: Date, noDays: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, noDays)
        return calendar.time
    }
}