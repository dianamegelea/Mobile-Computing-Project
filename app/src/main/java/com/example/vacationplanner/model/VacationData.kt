package com.example.vacationplanner.model

import android.os.Parcel
import android.os.Parcelable

data class VacationData (
    val cityName: String,
    val startDate: String,
    val imageBlob: String?,
    val noDays: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(cityName)
        p0.writeString(startDate)
        p0.writeString(imageBlob)
        p0.writeInt(noDays)
    }

    companion object CREATOR : Parcelable.Creator<VacationData> {
        override fun createFromParcel(parcel: Parcel): VacationData {
            return VacationData(parcel)
        }

        override fun newArray(size: Int): Array<VacationData?> {
            return arrayOfNulls(size)
        }
    }
}
