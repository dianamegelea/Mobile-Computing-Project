package com.example.vacationplanner.ui

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.R
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.utils.Utils
import com.example.vacationplanner.view.VacationAdapter
import com.example.vacationplanner.viewmodels.VacationViewModel
import com.example.vacationplanner.viewmodels.VacationViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var addButton : FloatingActionButton

    private val vacationViewModel: VacationViewModel by lazy {
        val application = requireNotNull(this.application) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, VacationViewModelFactory(application)).get(VacationViewModel::class.java)
    }

    private var vacationList: MutableList<VacationData> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vacationAdapter = VacationAdapter(vacationViewModel)

        val recView = findViewById<RecyclerView>(R.id.recycleview)
        recView.adapter = vacationAdapter
        recView.layoutManager = LinearLayoutManager(this)

        vacationViewModel.allVacations.observe(this) { vacation ->
            // Update the cached copy of the words in the adapter.
            vacation.let {
                vacationAdapter.vacationList = it.toMutableList()
                vacationAdapter.notifyDataSetChanged()
            }
        }
        addButton = findViewById(R.id.floatingButton)
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
                val vacationData = VacationData(name, date, null, null, days)

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
            if (!Utils.isOnline(application.applicationContext)) {
                Toast.makeText(
                    this,
                    "Please go online to check the weather",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!Utils.isDateWithinRange(start!!)) {
                Toast.makeText(
                    this,
                    "Wait until you get closer to the start date of your vacation",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, WeatherActivity::class.java)
                intent.putExtra("vacationdata", it)
                startActivity(intent)
            }
        }
    }
}