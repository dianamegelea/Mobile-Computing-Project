package com.example.vacationplanner

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.view.VacationAdapter
import com.example.vacationplanner.viewmodels.AppViewModel
import com.example.vacationplanner.viewmodels.AppViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var addButton : FloatingActionButton
    private lateinit var recView : RecyclerView

    private lateinit var appViewModel: AppViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //retrieve existing vacation details from shared prefs
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("vacationList", null)
        val list : List<VacationData>? = if (json != null) {
            val type = object : TypeToken<List<VacationData>>() {}.type
            Gson().fromJson<List<VacationData>>(json, type)
        } else {
            mutableListOf()
        }

        appViewModel = ViewModelProvider(this, AppViewModelFactory())[AppViewModel::class.java]

        vacationList = ArrayList(list.orEmpty())

        addButton = findViewById(R.id.floatingButton)
        recView = findViewById(R.id.recycleview)

        vacationAdapter = appViewModel.getVacationAdapter();

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
                val vacationData = VacationData(name, date, days)
                vacationList.add(vacationData)
                vacationAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Adding vacation...", Toast.LENGTH_SHORT).show()
                dialog.dismiss()

                val editor = sharedPreferences.edit()
                val updatedJson = Gson().toJson(vacationList)
                editor.putString("vacationList", updatedJson)
                editor.apply()

//                val daysDifference = getDaysDifference(date)
//                if (daysDifference <= 3) {
//                    displayNotification("Upcoming Vacation", "Your vacation to $name starts in $daysDifference days")
//                } else {
//                    val notificationId = System.currentTimeMillis().toInt()
//
//                    val calendar = Calendar.getInstance().apply {
//                        timeInMillis = System.currentTimeMillis()
//                        /*add(Calendar.DAY_OF_MONTH, daysDifference.toInt() - 3)
//                        set(Calendar.HOUR_OF_DAY, 0)
//                        set(Calendar.MINUTE, 0)
//                        set(Calendar.SECOND, 0)*/
//                        add(Calendar.MINUTE, 1) // for testing purposes
//                    }

//                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                    val intent = Intent(this, NotificationReceiver::class.java).apply {
//                        putExtra("notificationId", notificationId)
//                        putExtra("notificationTitle", "Upcoming Vacation")
//                        putExtra("notificationContent", "Your vacation to $name starts in 3 days")
//                    }
//                    val pendingIntent = PendingIntent.getBroadcast(this, vacationData.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//                }
            }

            addDialog.setNegativeButton("Cancel") {
                    dialog,_ ->
                dialog.dismiss()
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }
            addDialog.create()
            addDialog.show()
        }
//        vacationAdapter.downloadImageMethod

        vacationAdapter.onItemClick = fun(it: VacationData) {
            val start = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it.startDate)

            // the API only gives the forecast for 5 days on free version; this is what I'm checking here
            if (isDateWithinRange(start)) {
                val intent = Intent(this, WeatherForecastForCity::class.java)
                intent.putExtra("vacationdata", it)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Wait until you get closer to the start date of your vacation",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isDateWithinRange(startDate: Date): Boolean {
        val currentDate = Calendar.getInstance().time
        val endDate = calculateDateAfterNDays(currentDate, 5)
        return startDate <= endDate
    }

    private fun calculateDateAfterNDays(D: Date, N: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = D
        calendar.add(Calendar.DAY_OF_MONTH, N)
        return calendar.time
    }

    private fun getDaysDifference(startDate: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        val vacationDate = dateFormat.parse(startDate)
        val difference = vacationDate?.time?.minus(currentDate.time)
        return difference?.div(86400000) ?: 0
    }


    private fun displayNotification(title: String, content: String) {
        val notificationId = System.currentTimeMillis().toInt()
        val channelId = "VacationChannel"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel (required for Android 8.0 Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Vacation Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Display the notification
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        var vacationList: MutableList<VacationData> = mutableListOf()
        lateinit var vacationAdapter: VacationAdapter
    }
}