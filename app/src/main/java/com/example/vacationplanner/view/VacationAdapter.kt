package com.example.vacationplanner.view

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.vacationplanner.R
import com.example.vacationplanner.converters.Converters
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.repository.VacationRepository
import com.example.vacationplanner.repository.ImageRepository
import com.example.vacationplanner.viewmodels.VacationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VacationAdapter(private val vacationModel : VacationViewModel) : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {
    var onItemClick: ((VacationData) -> Unit)? = null

    var vacationList: MutableList<VacationData> = mutableListOf()

    inner class VacationViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val cityName: TextView = v.findViewById(R.id.cityname)
        val startDate: TextView = v.findViewById(R.id.startdate)
        val nrDays: TextView = v.findViewById(R.id.nrofdays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item, parent, false)
        return VacationViewHolder(v)
    }

    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        val vacationItem = vacationList[position]
        holder.cityName.text = vacationItem.cityName
        holder.startDate.text = vacationItem.startDate
        holder.nrDays.text = vacationItem.noDays.toString()

        if (vacationItem.imageBlob != null) {
            val bitmap = Converters.Base64ToBitmap(vacationItem.imageBlob)
            val drawable: Drawable = BitmapDrawable(holder.v.resources, bitmap)
            holder.v.background = drawable
        } else if (vacationItem.searchURL == null) {
            vacationModel.downloadLink(vacationItem.cityName)
        } else {
            Glide.with(holder.v.context).load(vacationItem.searchURL)
                .into(object : CustomViewTarget<View, Drawable>(holder.v) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        // Handle failed image loading if needed
                    }

                    override fun onResourceReady(
                        resource: Drawable, transition: Transition<in Drawable>?
                    ) {
                        view.background = resource
                        vacationModel.updateImage(vacationItem.cityName, resource)
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {
                        // Not necessary in this case
                    }
                })
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(vacationItem)
        }
    }

    override fun getItemCount(): Int {
        return vacationList.size
    }
}