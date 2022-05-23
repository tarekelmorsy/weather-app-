package com.example.weatherapp

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
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.prsentation.weather.setting.SettingActivity
import com.example.weatherapp.prsentation.weather.view.ui.favorite.MapsActivity
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val navView: BottomNavigationView = binding.navView
        val language: String? = sharedPreferences.getString(LANGUAGE, "0")
        var langInt=sharedPreferences.getInt(LANGUAGEINT, 0)

      //  Log.d("onCreate",  langInt.toString()+"onCreate: ")
      //  Toast.makeText(this , langInt.toString(),Toast.LENGTH_LONG).show()

        if (language.equals("ar")){

            setLocate("ar")
            if (langInt.equals(1)){
                recreate()
                editor.putInt(LANGUAGEINT, 2)
              //  Log.d("onCreate",  langInt.toString()+"onCreate: ")

              // Toast.makeText(this , langInt.toString(),Toast.LENGTH_LONG).show()
                editor.apply()
                editor.commit()
            }
        }else {
            setLocate("en")
            // recreate()
            if (langInt.equals(1)){
                recreate()
                editor.putInt(LANGUAGEINT, 2)
                editor.apply()
                editor.commit()
            }

        }


        //change language
        loadLocate()
        //tool bar
        var action =supportActionBar
        action?.title= getText(R.string.app_name)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
           R.id.setting->
           {
               val intent = Intent(this, SettingActivity::class.java)
               startActivity(intent)
               finish()
           }
        }
        return super.onOptionsItemSelected(item)
    }

}