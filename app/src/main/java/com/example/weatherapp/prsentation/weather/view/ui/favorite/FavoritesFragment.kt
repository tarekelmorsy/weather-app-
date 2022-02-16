package com.example.weatherapp.prsentation.weather.view.ui.favorite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.local.ModelFavorite
import com.example.weatherapp.data.local.NetworkConnection
import com.example.weatherapp.databinding.FragmentFavoritesBinding
import com.example.weatherapp.prsentation.weather.view.ui.notifications.ModelAlarm
import com.google.android.libraries.places.api.Places


import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.mcit.kmvvm.data.remote.*
import java.util.*
import kotlin.collections.ArrayList

class FavoritesFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private var _binding: FragmentFavoritesBinding? = null
    private lateinit var adapterFavorite: AdapterFavorite
    var placeFields: List<Place.Field>? = null
    val STARTKEY = 100
    private var lat = 0.0
    private var long = 0.0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoriteViewModel =
            ViewModelProvider(this).get(FavoriteViewModel::class.java)

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        adapterFavorite = AdapterFavorite(ArrayList(), favoriteViewModel)
        binding?.rcFavroite?.layoutManager =
            GridLayoutManager(context, 2)
        binding?.rcFavroite?.adapter = adapterFavorite

        if (!Places.isInitialized()) {
            Places.initialize(context, KEYMAP)
        }

        binding?.edSearch.setFocusable(false)
        binding?.edSearch.setOnClickListener(View.OnClickListener { v: View? ->
            placeFields = Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.NAME
            )
            val intent =
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeFields)
                    .build(activity)
            startActivityForResult(intent, STARTKEY)
        })

        activity?.let { it1 ->
            favoriteViewModel.allFavorite.observe(it1, androidx.lifecycle.Observer {

                adapterFavorite.setFavorite(it as ArrayList<ModelFavorite>)
                adapterFavorite.notifyDataSetChanged()

                if (it.size > 0) {
                    binding?.iconFavorite.visibility = View.GONE


                } else
                    binding?.iconFavorite.visibility = View.VISIBLE


            })
        }




        binding?.swiperRefresh.setOnRefreshListener {

            binding?.swiperRefresh.isRefreshing = false
        }


        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
//        activity?.let { it1 ->
//            val networkConnection = NetworkConnection(it1)
//            networkConnection.observe(it1, Observer { isconection ->
//                if (isconection) {
//                    Toast.makeText(it1, "Connected", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(it1, "Disconnected", Toast.LENGTH_SHORT).show()
//
//                }
//            })
//        }

    }


    private fun observeViewModel() {


        favoriteViewModel.currentWeatherLiveData.observe(viewLifecycleOwner, Observer {

            initViews()
            binding?.btSave.setOnClickListener { v ->
                 if (lat != 0.0) {


                    favoriteViewModel.insert(
                        ModelFavorite(
                            it.main.temp,
                            it.name,
                            it.sys.country?:"",
                            it.main.humidity,
                            it.wind.speed,
                            it.weather[0].icon,
                            it.weather[0].main
                        )
                    )
                    lat = 0.0
                    binding?.edSearch.setText("")
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.please_enter_city),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

        })

        activity?.let { it2 ->
            favoriteViewModel.allFavorite.observe(
                it2,
                androidx.lifecycle.Observer {


                    adapterFavorite.setFavorite(it as ArrayList<ModelFavorite>)
                    adapterFavorite.notifyDataSetChanged()
                    if (it.size > 0) {
                        binding?.iconFavorite.visibility = View.GONE


                    } else
                        binding?.iconFavorite.visibility = View.VISIBLE

                })
        }


    }


    private fun initViews() {
        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences(
            "sharedPrefFile",
            Context.MODE_PRIVATE
        )

        val temp = sharedPreferences.getString(TEMP, "")
        val wind = sharedPreferences.getString(WIND, "")
        val language = sharedPreferences.getString(LANGUAGE, "")

            if (temp != null && language != null) {
                favoriteViewModel.getCurrentWeather(lat, long, temp, language)

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STARTKEY && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data)
            binding?.edSearch.setText(place.address)
            lat = place.latLng.latitude
            long = place.latLng.longitude


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            val status = Autocomplete.getStatusFromIntent(data)
            Toast.makeText(activity, status.statusMessage, Toast.LENGTH_SHORT).show()
        }
    }
}