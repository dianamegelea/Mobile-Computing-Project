package com.example.vacationplanner.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.util.Calendar
import java.util.Date

class Utils {

    companion object {
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

        fun isDateWithinRange(startDate: Date): Boolean {
            val currentDate = Calendar.getInstance().time
            val endDate = calculateDateAfterNDays(currentDate, 5)
            return startDate <= endDate
        }

        fun calculateDateAfterNDays(date: Date, noDays: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_MONTH, noDays)
            return calendar.time
        }
    }
}