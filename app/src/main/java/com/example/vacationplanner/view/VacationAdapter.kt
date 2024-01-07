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
import com.example.vacationplanner.viewmodels.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VacationAdapter : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {
    var onItemClick: ((VacationData) -> Unit)? = null

    var vacationList: MutableList<VacationData> = mutableListOf()

    lateinit var imageRepository : ImageRepository
    lateinit var vacationRepository: VacationRepository

    fun setData(vacationList: MutableList<VacationData>) {
        this.vacationList = vacationList
    }

    inner class VacationViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
        val city_name = v.findViewById<TextView>(R.id.cityname)
        val start_date = v.findViewById<TextView>(R.id.startdate)
        val nr_days = v.findViewById<TextView>(R.id.nrofdays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item, parent, false)
        return VacationViewHolder(v)
    }

//    fun setDownloadImageMethod(downloadImageMethod: ((query: String, apiKey: String, cx: String) -> String)) {
//        this.downloadImageMethod = downloadImageMethod
//    }
    private suspend fun downloadImage(query: String, apiKey: String, cx: String): String? {
        try {
            val response = imageRepository.searchImage(query, apiKey, cx)
            val items = response.items
            if (items.isNotEmpty()) {
                return items[0].link
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onBindViewHolder(holder: VacationViewHolder, position: Int) {
        val vacationItem = vacationList[position]
        holder.city_name.text = vacationItem.cityName
        holder.start_date.text = vacationItem.startDate
        holder.nr_days.text = vacationItem.noDays.toString()

        if (vacationItem.imageBlob != null) {
            val bitmap = Converters.Base64ToBitmap(vacationItem.imageBlob)
            val drawable: Drawable = BitmapDrawable(holder.v.resources, bitmap)
            holder.v.background = drawable
        } else {
            // Download image from Google search and set it as the background
            CoroutineScope(Dispatchers.IO).launch {
                val imageUrl = downloadImage(
                    "most popular tourist attraction in " + vacationItem.cityName,
                    "AIzaSyBj7YqeG3o2r0z4KoSN1rXWuh-EC0iJQeI",
                    "63e20becb9609444f"
                )

                withContext(Dispatchers.Main) {
                    if (imageUrl != null) {
                        Glide.with(holder.v.context).load(imageUrl)
                            .into(object : CustomViewTarget<View, Drawable>(holder.v) {
                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    // Handle failed image loading if needed
                                }

                                override fun onResourceReady(
                                    resource: Drawable, transition: Transition<in Drawable>?
                                ) {

                                    val bitmap = Bitmap.createScaledBitmap((resource as BitmapDrawable).bitmap, 120, 120, false)
                                    val bitmapBase64Encoded = Converters.BitmapToBase64(bitmap)

                                    view.background = resource
                                    CoroutineScope(Dispatchers.IO).launch {
                                        vacationRepository.updateVacationImage(vacationItem.cityName, bitmapBase64Encoded!!)
                                    }
                                }

                                override fun onResourceCleared(placeholder: Drawable?) {
                                    // Not necessary in this case
                                }
                            })
                    }
                }
            }
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(vacationItem)
        }
    }

    override fun getItemCount(): Int {
        return vacationList.size
    }
}