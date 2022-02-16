package com.example.weatherapp.prsentation.weather.view.ui.favorite

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mcit.kmvvm.data.remote.LAt
import com.mcit.kmvvm.data.remote.LOCATIONCLICK
import com.mcit.kmvvm.data.remote.LONG
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var geocoder: Geocoder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera



        mMap.setOnMapClickListener {latLong->
            val markerOptions=MarkerOptions()

            markerOptions.position(latLong)
            markerOptions.title("${latLong.latitude} : ${latLong.longitude}")
            mMap.clear()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong,10f))
            mMap.addMarker(markerOptions)
            val address=geocoder?.getFromLocation(latLong.latitude,latLong.longitude,1)
            Log.d("onMapReady", "onMapReady: ${address?.get(0)?.getAddressLine(0)}")

            // show dialog
            val dialog = DialogPlus.newDialog(this)
                .setContentHolder(ViewHolder(R.layout.map_layout))
                .setExpanded(true, 700)
                .create()
            dialog.show()
            val view = dialog.holderView
            val btCancle = view.findViewById<TextView>(R.id.bt_cancle)
            val tvNameCity = view.findViewById<TextView>(R.id.tvNameCity)
            val latLongCity = view.findViewById<TextView>(R.id.latLongCity)
            val btSave = view.findViewById<Button>(R.id.bt_save)
            tvNameCity.text=getString(R.string.address)+"${address?.get(0)?.getAddressLine(0)}"


            latLongCity.text=getString(R.string.latitude)+"${latLong.longitude}\n"+getString(R.string.longitude)+"${latLong.longitude}"
            btCancle.setOnClickListener {
                dialog.dismiss()

            }
            btSave.setOnClickListener {
                val sharedPreferences: SharedPreferences =
                    this.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putFloat(LAt,latLong.latitude.toFloat())
                editor.putFloat(LONG, latLong.longitude.toFloat())
                editor.putInt(LOCATIONCLICK, 3)
                editor.apply()
                editor.commit()
                dialog.dismiss()
                finish()
                Toast.makeText(this,"updated location",Toast.LENGTH_LONG).show()
            }

        }
    }
}