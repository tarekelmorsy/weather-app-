package com.example.weatherapp.prsentation.weather.view.ui.home

import Hourly
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.text.DateFormat

class AdapterHours(var listHours: List<Hourly>) : RecyclerView.Adapter<AdapterHours.ViewHolder>() {
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvHour: TextView
            get() = view.findViewById(R.id.tvHourH)
        val imIconH: ImageView
            get() = view.findViewById(R.id.imIconH)
        val tvTempH: TextView
            get() = view.findViewById(R.id.tvTempH)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viwe: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_hour, parent, false)
        return ViewHolder(viwe)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.imIconH.context)
            .load("http://openweathermap.org/img/wn/${listHours.get(position).weather[0].icon}@2x.png")
            .into(holder.imIconH)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val input = java.time.format.DateTimeFormatter.ISO_INSTANT
                .format(java.time.Instant.ofEpochSecond(listHours.get(position).dt.toLong()))

            val arrOfStr: Array<String> = input.split("T").toTypedArray()
            var s: String = arrOfStr[1]

            // val input = "17:03:13"

            Log.d("timeee", s)
            val inputFormat: DateFormat = SimpleDateFormat("HH:mm:ss")

            val outputFormat: DateFormat = SimpleDateFormat("KK:mm a")

            holder.tvHour.text = outputFormat.format(inputFormat.parse(s)).toString()

        }
        holder.tvTempH.text = "${listHours.get(position).temp.toInt()}°C"
    }

    override fun getItemCount(): Int = listHours.size
}