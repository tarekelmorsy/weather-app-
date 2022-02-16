package com.example.weatherapp.prsentation.weather.view.ui.home

import Daily
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


class AdapterDays(var listDays:List<Daily>): RecyclerView.Adapter<AdapterDays.ViewHolder>()  {
    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tvNameDays: TextView
            get() = view.findViewById(R.id.tvNameDay)
        val ivIconDay: ImageView
            get() = view.findViewById(R.id.ivIconDay)
        val tvTempMax: TextView
            get() = view.findViewById(R.id.tvTempMax)
        val tvTempMin: TextView
            get() = view.findViewById(R.id.tvTempMin)
        val tvDescription: TextView
            get() = view.findViewById(R.id.tvDescription2)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viwe: View = LayoutInflater.from(parent.context).inflate(R.layout.itme_days,parent,false)
        return ViewHolder(viwe)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.ivIconDay.context).load("http://openweathermap.org/img/wn/${listDays.get(position).weather[0].icon}@2x.png").into(holder.ivIconDay)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val input = java.time.format.DateTimeFormatter.ISO_INSTANT
                .format(java.time.Instant.ofEpochSecond(listDays.get(position).dt.toLong()))

            val inFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inFormat.parse(input)


            val arrOfStr: Array<String> = date.toString().split(" ").toTypedArray()
            var dateString:String=arrOfStr[0]
           // val outFormat = SimpleDateFormat("EEEE")
          //  val goal = outFormat.format(date)
            Log.d("timeee",""+dateString)
            if (dateString.equals("Sat")){
                holder.tvNameDays.text= holder.ivIconDay.context.getText(R.string.saturday)
            }else if (dateString.equals("Mon")){
                holder.tvNameDays.text = holder.ivIconDay.context.getText(R.string.monday)
            }else if (dateString.equals("Tue")){
                holder.tvNameDays.text = holder.ivIconDay.context.getText(R.string.tuesday)
            }else if (dateString.equals("Wed")){
                holder.tvNameDays.text = holder.ivIconDay.context.getText(R.string.wednesday)
            }else if (dateString.equals("Thu")){
                holder.tvNameDays.text = holder.ivIconDay.context.getText(R.string.thursday)
            }else if (dateString.equals("Sun")){
                holder.tvNameDays.text = holder.ivIconDay.context.getText(R.string.sunday)
            }else if (dateString.equals("Fri")){
                holder.tvNameDays.text =holder.ivIconDay.context.getText(R.string.friday)
            }
                // holder.tvHour.text=input
        }
       // holder.tvNameDays.text="sunday"
        holder.tvTempMax.text="${listDays.get(position).temp.max.toInt()}°C"
        holder.tvTempMin.text="${listDays.get(position).temp.min.toInt()}°C"
        holder.tvDescription.text="${listDays.get(position).weather.get(0).description}"
    }

    override fun getItemCount(): Int =listDays.size
}