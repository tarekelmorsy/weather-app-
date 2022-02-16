package com.example.weatherapp.prsentation.weather.view.ui.home

import Daily
import Hourly
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.prsentation.weather.view.ui.favorite.MapsActivity
import com.github.matteobattilana.weather.PrecipType
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.mcit.kmvvm.data.remote.*
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private var binding: FragmentHomeBinding? = null
    lateinit var weather: PrecipType
    var placeFields: List<Place.Field>? = null
    var PERMISSION_ID = 44
    val STARTKEY = 100
    var latitude = 0.0
    var longitude = 0.0

    private lateinit var adapterHours: AdapterHours
    private lateinit var adapterDays: AdapterDays
    private lateinit var sharedPreferences: SharedPreferences
    var weatherSpeed = 0
    var lat: Double = 0.0
    var long: Double = 0.0
    var weatherParticles = 0f

    var mFusedLocationClient: FusedLocationProviderClient? = null


    var locationRequest: LocationRequest? = null

    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            for (location in locationResult.locations) {
                Log.d("TAG", "onLocationResult: $location")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.let {
            viewModel = ViewModelProvider(it).get(WeatherViewModel::class.java)



        }

        binding = FragmentHomeBinding.inflate(inflater)
        adapterHours = AdapterHours(listOf<Hourly>())
        binding?.reHour?.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding?.reHour?.adapter = adapterHours


        adapterDays = AdapterDays(listOf<Daily>())
        binding?.reDays?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding?.reDays?.adapter = adapterDays


            //location init
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            locationRequest = LocationRequest.create()
            locationRequest!!.interval = 2000
            locationRequest!!.fastestInterval = 1000
            locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences(
            "sharedPrefFile",
            Context.MODE_PRIVATE
        )
        viewModel.weatherLiveData.observe(viewLifecycleOwner, Observer {
            //  Log.d(TAG, "observeViewModel: ${it.current?.temperature}")
            context?.let { context ->
                Glide.with(context)
                    .load("http://openweathermap.org/img/wn/${it.current.weather[0].icon}@2x.png")
                    .into(binding?.ivWeather!!)
            }
            //set data in text view

            binding?.tvHumidity?.text = "${it.current.humidity.toInt()}%"
            binding?.tvPressure?.text = "${it.current.pressure.toInt()}hpa"
            binding?.tvCloud?.text = "${it.current.clouds.toInt()}%"
            binding?.tvVisibility?.text = "${it.current.visibility.toInt()}m  "
            binding?.tvUvi?.text = "${it.current.uvi}"


            if(it.current.weather.get(0).main.equals("Rain")){
                weather = PrecipType.RAIN
                 weatherParticles = 60f
                weatherSpeed = 600

            }else if(it.current.weather.get(0).main.equals("Snow")	){
                weather = PrecipType.SNOW
                 weatherParticles = 10f
                weatherSpeed = 200
            }else
            {
                weather = PrecipType.CLEAR

            }


             binding?.weatherView?.apply {
                 setWeatherData(weather)
                speed = weatherSpeed // How fast
                emissionRate = weatherParticles // How many particles
                angle = 0 // The angle of the fall
                fadeOutPercent = .75f // When to fade out (0.0f-1.0f)
            }
            adapterHours.listHours = it.hourly
            adapterDays.listDays = it.daily
            adapterDays.notifyDataSetChanged()
            adapterHours.notifyDataSetChanged()


        })

        viewModel.currentWeatherLiveData.observe(viewLifecycleOwner, Observer {

            val sharedPreferences: SharedPreferences =
                activity!!.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            val isChoose: Int = sharedPreferences.getInt(CHOOSE, 0)

            if (isChoose != 1) {
                val dialog = DialogPlus.newDialog(activity)
                    .setContentHolder(ViewHolder(R.layout.choose_location))
                    .setExpanded(true, 1000)
                    .create()
                dialog.show()

                val view = dialog.holderView
                val tvMapCh = view.findViewById<TextView>(R.id.tvMapCh)
                val tvSearchCh = view.findViewById<TextView>(R.id.tvSearchCh)

                tvSearchCh.setOnClickListener {
                    placeFields =
                        Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
                    val intent =
                        Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeFields)
                            .build(activity!!)
                    startActivityForResult(intent, STARTKEY)

                    dialog.dismiss()
                    initViews()
                    observeViewModel()

                }
                tvMapCh.setOnClickListener {
                    val intent = Intent(activity!!, MapsActivity::class.java)
                    startActivity(intent)
                    editor.putInt(LOCATIONCLICK, 3)
                    editor.putInt(CHOOSE, 1)
                    editor.apply()
                    editor.commit()

                    dialog.dismiss()
                    initViews()
                    observeViewModel()

                }

            }

            if (it!=null) {
                binding?.tvCurrentName?.text = it.name
                binding?.tvDescription?.text = it.weather[0].description
                binding?.tvTitleTemp?.text = "${it.main.temp.toInt()}°C"
            } })
        viewModel.windWeatherLiveData.observe(viewLifecycleOwner, Observer {
            if (sharedPreferences.getString(WIND, "").equals("imperial"))
                binding?.tvWind?.text = "${it.current.wind_gust.toInt()}m/h"
            else
                binding?.tvWind?.text = "${it.current.wind_gust.toInt()}m/s"


        })
    }

    private fun initViews() {

        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences(
            "sharedPrefFile",
            Context.MODE_PRIVATE
        )
          lat = sharedPreferences.getFloat(LAt, -1f).toDouble()
          long = sharedPreferences.getFloat(LONG, -1f).toDouble()
        val temp = sharedPreferences.getString(TEMP, "")
        val wind = sharedPreferences.getString(WIND, "")
        val language = sharedPreferences.getString(LANGUAGE, "")


        Log.d("TAG", "initViews: " + lat)
        Log.d("TAG", "temp: " + temp)

        if (temp != null&&language!=null) {
            viewModel.getWeather(lat, long, temp,language)
            viewModel.getDaysWeather(lat, long, "hourly",language)
            activity?.let { it1 ->
            viewModel.getCurrentWeather(lat, long, temp, language,it1)}
            wind?.let { viewModel.getWindWeather(lat, long, it,language) }

        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STARTKEY && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data)
            lat = place.latLng.latitude
            long = place.latLng.longitude
            val sharedPreferences: SharedPreferences =
                activity!!.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putFloat(LAt, lat.toFloat())
            editor.putFloat(LONG, long.toFloat())
            editor.putInt(LOCATIONCLICK, 2)
            editor.putInt(CHOOSE,1)
            editor.apply()
            editor.commit()
            Log.d("TAG", "onActivityResult: " + lat)
            Log.d("TAG", "onActivityResult: " + long)


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            val status = Autocomplete.getStatusFromIntent(data)
            Toast.makeText(activity!!, status.statusMessage, Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val TAG = "WeatherFragment"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}