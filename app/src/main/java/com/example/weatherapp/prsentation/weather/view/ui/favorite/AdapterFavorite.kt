package com.example.weatherapp.prsentation.weather.view.ui.favorite

import CurrentWeather
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.weatherapp.R
import com.example.weatherapp.data.local.ModelFavorite
import com.example.weatherapp.prsentation.weather.view.ui.notifications.ModelAlarm
import com.github.matteobattilana.weather.PrecipType
import java.lang.String

class AdapterFavorite(
    var listFavorites: ArrayList<ModelFavorite>,
    val viewModel: FavoriteViewModel
) : RecyclerView.Adapter<AdapterFavorite.ViewHolder>() {


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tvTempFav: TextView
            get() = view.findViewById(R.id.tvTempFav)
        val imIconFv: ImageView
            get() = view.findViewById(R.id.imIconFv)
        val tvCurrentFav: TextView
            get() = view.findViewById(R.id.tvCurrentFav)
        val tvCityFv: TextView
            get() = view.findViewById(R.id.tvCityFv)
        val tv_humidityFv: TextView
            get() = view.findViewById(R.id.tv_humidityFv)
        val tv_windFv: TextView
            get() = view.findViewById(R.id.tv_windFv)
        val ivDelete: ImageView
            get() = view.findViewById(R.id.ivDelete)
        val weather_view_favorite: com.github.matteobattilana.weather.WeatherView
            get() = view.findViewById(R.id.weather_view_favorite)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viwe: View =
            LayoutInflater.from(parent.context).inflate(R.layout.itme_favorites, parent, false)
        return ViewHolder(viwe)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.imIconFv.context)
            .load("http://openweathermap.org/img/wn/${listFavorites.get(position).icon}@2x.png")
            .into(holder.imIconFv)

        holder.apply {
            tvCityFv.text = "${listFavorites.get(position).name}"
            tvCurrentFav.text = "${listFavorites.get(position).country}"
            tvTempFav.text = "${listFavorites.get(position).temp.toInt()}°C"
            tv_humidityFv.text = "${listFavorites.get(position).humidity}%"
            tv_windFv.text = "${listFavorites.get(position).wind.toInt()}m/s"

        }


        holder.ivDelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.imIconFv.getContext())
            builder.setTitle(holder.imIconFv.getContext().getString(R.string.are_you_sure))
            builder.setMessage(holder.imIconFv.getContext().getString(R.string.delete_data))
            builder.setPositiveButton(
                holder.imIconFv.getContext().getString(R.string.delete)
            ) { dialog: DialogInterface?, which: Int ->

                viewModel.deleteFavorite(listFavorites.get(position))
                listFavorites.removeAt(position)
                notifyDataSetChanged()

            }
            builder.setNegativeButton(
                holder.imIconFv.getContext().getString(R.string.cancel)
            ) { dialog: DialogInterface?, which: Int ->
                Toast.makeText(
                    holder.imIconFv.getContext(),
                    holder.imIconFv.getContext().getString(R.string.canclled),
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()


        }
        lateinit var weather: PrecipType
        var weatherSpeed = 0
        var weatherParticles = 0f
        if (listFavorites.get(position).main.equals("Rain")) {
            weather = PrecipType.RAIN
            weatherParticles = 60f
            weatherSpeed = 600

        } else if (listFavorites.get(position).main.equals("Rain")) {
            weather = PrecipType.SNOW
            weatherParticles = 10f
            weatherSpeed = 200
        } else {
            weather = PrecipType.CLEAR

        }


        holder.weather_view_favorite.apply {
            setWeatherData(weather)
            speed = weatherSpeed // How fast
            emissionRate = weatherParticles // How many particles
            angle = 0 // The angle of the fall
            fadeOutPercent = .75f // When to fade out (0.0f-1.0f)
        }

    }

    fun setFavorite(favorite: ArrayList<ModelFavorite>) {
        this.listFavorites = favorite
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listFavorites.size
}