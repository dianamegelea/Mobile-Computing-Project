package com.example.vacationplanner.view

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.R
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.viewmodels.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VacationAdapter : RecyclerView.Adapter<VacationAdapter.VacationViewHolder>() {
    var onItemClick: ((VacationData) -> Unit)? = null

    private var vacationList = ArrayList<VacationData>()

    lateinit var imageRepository : ImageRepository

//    fun setImageRepository(imageRepository : ImageRepository) {
//        this.imageRepository = imageRepository
//    }

    fun setData(vacationList: ArrayList<VacationData>) {
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

        // Download image from Google search and set it as the background
        CoroutineScope(Dispatchers.IO).launch {
            val imageUrl = downloadImage(
                "most popular tourist attraction in " + vacationItem.cityName,
                "AIzaSyCR5jigFZQaOMJj_neagC2f9fwIzC5sFeo",
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
                                view.background = resource
                            }

                            override fun onResourceCleared(placeholder: Drawable?) {
                                // Not necessary in this case
                            }
                        })
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