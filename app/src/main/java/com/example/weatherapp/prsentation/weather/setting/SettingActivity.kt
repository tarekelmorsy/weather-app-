package com.example.weatherapp.prsentation.weather.setting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivitySettingBinding
import com.example.weatherapp.prsentation.weather.view.ui.favorite.MapsActivity
import com.example.weatherapp.prsentation.weather.view.ui.notifications.NotificationsViewModel
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.mcit.kmvvm.data.remote.*
import java.util.*

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    val STARTKEY = 100
    private var lat = 0.0
    private var long = 0.0
    var placeFields: List<Place.Field>? = null
    var PERMISSION_ID = 44
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var latitude = 0.0
    var longitude = 0.0


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
        loadLocate() // call LoadLocate

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //tool bar
        var action =supportActionBar
        action?.title=getText(R.string.setting)
        //location init
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest!!.interval = 2000
        locationRequest!!.fastestInterval = 1000
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

         val language: Int = sharedPreferences.getInt(LANGUCLICK, 0)
        val windClick: Int = sharedPreferences.getInt(WINDKLICK, 0)
        val tempClick: Int = sharedPreferences.getInt(TEMPCLICK, 0)
        val notification: Int = sharedPreferences.getInt(NOTIFICATION, 0)


        ii=0
//handle  language checked
        if (language.equals(1)) {
            binding.tvAr.isChecked = true
            binding.tvEg.isChecked = false

        } else if (language.equals(2)) {
            binding.tvEg.isChecked = true
            binding.tvAr.isChecked = false

        }
//handle  windClicked
        if (windClick.equals(1)) {
            binding.tvSec.isChecked = true
            binding.tvHour.isChecked = false

        } else if (windClick.equals(2)) {
            binding.tvHour.isChecked = true
            binding.tvSec.isChecked = false

        }
//handle Temp clicked
        if (tempClick.equals(1)) {
            binding.rbCelsius.isChecked = true
            binding.rbKelvin.isChecked = false
            binding.rbFahrenheit.isChecked = false

        } else if (tempClick.equals(2)) {
            binding.rbCelsius.isChecked = false
            binding.rbKelvin.isChecked = true
            binding.rbFahrenheit.isChecked = false
        } else if (tempClick.equals(3)) {
            binding.rbCelsius.isChecked = false
            binding.rbKelvin.isChecked = false
            binding.rbFahrenheit.isChecked = true

        }
        //handle notification
        if (notification.equals(1)) {
            binding.notificationEnable.isChecked = true
            binding.notificationDisable.isChecked = false


        }else if(notification.equals(2)){
            binding.notificationEnable.isChecked = false
            binding.notificationDisable.isChecked = true

        }

            val locationClick: Int = sharedPreferences.getInt(LOCATIONCLICK, 0)

         if (locationClick.equals(1)) {
            binding.tvGps.isChecked = true
            binding.tcSearch.isChecked = false
            binding.tvMap.isChecked = false


        } else if (locationClick.equals(2)) {
            binding.tcSearch.isChecked = true
            binding.tvGps.isChecked = false
            binding.tvMap.isChecked = false


        }else if (locationClick.equals(3)) {
            binding.tcSearch.isChecked = false
            binding.tvGps.isChecked = false
            binding.tvMap.isChecked = true


        }


        if (!Places.isInitialized()) {
            Places.initialize(this, KEYMAP)
        }
        binding.tvTitleLocation.setOnClickListener {
            changColor(1)
        }
        binding.tvTitleWind.setOnClickListener { changColor(2) }
        binding.tvTitleTemp.setOnClickListener { changColor(3) }


        binding?.tvGps.setOnClickListener {
            getLastLocation()
            Toast.makeText(
                this@SettingActivity,
                latitude.toString() + "  " + longitude,
                Toast.LENGTH_SHORT
            ).show()
            editor.putFloat(LAt, latitude.toFloat())
            editor.putFloat(LONG, longitude.toFloat())
            editor.putInt(LOCATIONCLICK, 1)
            editor.apply()
            editor.commit()


        }

        binding?.tvMap.setOnClickListener {

            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            editor.putInt(LOCATIONCLICK, 3)
            editor.apply()
            editor.commit()


        }

        binding.tcSearch.setOnClickListener {
            placeFields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeFields)
                .build(this)
            startActivityForResult(intent, STARTKEY)

        }


        binding.tvSec.setOnClickListener {
            editor.putString(WIND, "")
            editor.putInt(WINDKLICK, 1)
            editor.apply()
            editor.commit()
        }
        binding.tvHour.setOnClickListener {
            editor.putString(WIND, "imperial")
            editor.putInt(WINDKLICK, 2)
            editor.apply()
            editor.commit()
        }
        binding.rbCelsius.setOnClickListener {

            editor.putString(TEMP, "metric")
            editor.putInt(TEMPCLICK, 1)
            editor.apply()
            editor.commit()
        }


        binding.rbKelvin.setOnClickListener {
            editor.putString(TEMP, "")
            editor.putInt(TEMPCLICK, 2)
            editor.apply()
            editor.commit()

        }
        binding.rbFahrenheit.setOnClickListener {
            editor.putString(TEMP, "imperial")
            editor.putInt(TEMPCLICK, 3)
            editor.apply()
            editor.commit()
        }
        //  binding.tv2.setTextColor(ContextCompat.getColor(this, R.color.text_api))
        //binding.tv1.setTextColor(ContextCompat.getColor(this, R.color.text_title))
        binding?.la.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding?.tvAr.setOnClickListener {
            editor.putInt(LANGUCLICK, 1)
            editor.putString(LANGUAGE, "ar")
            editor.putInt(LANGUAGEINT, 1)
            editor.apply()
            editor.commit()
            setLocate("ar")
            recreate()

        }
        binding?.tvEg.setOnClickListener {
            editor.putInt(LANGUCLICK, 2)
            editor.putString(LANGUAGE, "en")
            editor.putInt(LANGUAGEINT, 1)
            editor.apply()
            editor.commit()
            setLocate("en")
            recreate()

        }
        binding?.notificationEnable.setOnClickListener {
            editor.putInt(NOTIFICATION, 1)
            editor.apply()
            editor.commit()


        }
        binding?.notificationDisable.setOnClickListener {
                var notificationsViewModel= ViewModelProvider(this).get(NotificationsViewModel::class.java)
            notificationsViewModel.deleteAllAlarm()
            val workManager = WorkManager.getInstance()
            workManager.cancelAllWork()
            editor.putInt(NOTIFICATION, 2)
            editor.apply()
            editor.commit()


        }
    }

    fun changColor(i: Int) {


        when (i) {
            1 -> {
                binding.tvTitleLocation.setTextColor(ContextCompat.getColor(this, R.color.bg_api))
                binding.tvTitleTemp.setTextColor(ContextCompat.getColor(this, R.color.text_title))
                binding.tvTitleWind.setTextColor(ContextCompat.getColor(this, R.color.text_title))
                binding.radioGroupLocation.visibility = View.VISIBLE
                binding.radioGroupTemp.visibility = View.GONE
                binding.radioGroupWind.visibility = View.GONE


            }
            2 -> {
                binding.tvTitleWind.setTextColor(ContextCompat.getColor(this, R.color.bg_api))
                binding.tvTitleTemp.setTextColor(ContextCompat.getColor(this, R.color.text_title))
                binding.tvTitleLocation.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_title
                    )
                )

                binding.radioGroupLocation.visibility = View.GONE
                binding.radioGroupTemp?.visibility = View.GONE
                binding.radioGroupWind.visibility = View.VISIBLE
            }
            3 -> {
                binding.tvTitleTemp.setTextColor(ContextCompat.getColor(this, R.color.bg_api))
                binding.tvTitleWind.setTextColor(ContextCompat.getColor(this, R.color.text_title))
                binding.tvTitleLocation.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_title
                    )
                )
                binding.radioGroupLocation.visibility = View.GONE
                binding.radioGroupTemp.visibility = View.VISIBLE
                binding.radioGroupWind.visibility = View.GONE

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STARTKEY && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data)
            lat = place.latLng.latitude
            long = place.latLng.longitude
            val sharedPreferences: SharedPreferences =
                this.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putFloat(LAt, lat.toFloat())
            editor.putFloat(LONG, long.toFloat())
            editor.putInt(LOCATIONCLICK, 2)
            editor.apply()
            editor.commit()
            Log.d("TAG", "onActivityResult: " + lat)
            Log.d("TAG", "onActivityResult: " + long)


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            val status = Autocomplete.getStatusFromIntent(data)
            Toast.makeText(this, status.statusMessage, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {
                locationRequest?.let {
                    mFusedLocationClient?.requestLocationUpdates(
                        it,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
                val locationTask: Task<Location> = mFusedLocationClient!!.getLastLocation()
                locationTask.addOnSuccessListener { location ->
                    if (location != null) {
                        //We have a location
                        latitude = location.latitude
                        longitude = location.longitude
                    } else {
                        Log.d("log", "onSuccess: Location was null...")
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.turnOn), Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions()
        }
    }


    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    /**
     * change language
     */
    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        language?.let { setLocate(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.back->
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation()
        }
    }
}