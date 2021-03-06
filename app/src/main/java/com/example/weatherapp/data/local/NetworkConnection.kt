package com.example.weatherapp.data.local

import android.annotation.TargetApi
import android.content.*
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

/**
 *  this class works as  check Network Connection
 */
class NetworkConnection (private val context: Context):LiveData<Boolean>(){
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
private lateinit var networkCallback:ConnectivityManager.NetworkCallback


    override fun onActive() {
        super.onActive()
        updateConnection()
        when{
           Build.VERSION.SDK_INT >= Build.VERSION_CODES.N->{

               connectivityManager.registerDefaultNetworkCallback(connect())
           } Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP->{

               lollipopNetwork()

           }
            else->{

                context.registerReceiver(
                    networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }

            }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            connectivityManager.unregisterNetworkCallback(connect())

        }else{

            context.unregisterReceiver(networkReceiver)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    fun lollipopNetwork(){

        val requestBuild=NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        connectivityManager.registerNetworkCallback(
            requestBuild.build(),connect()
        )
    }
private fun connect():ConnectivityManager.NetworkCallback{



    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        networkCallback=object :ConnectivityManager.NetworkCallback(){

            override fun onLost(network: Network) {
                    super.onLost(network)
                postValue(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }
        }
        return networkCallback
    }else
        throw IllegalAccessError("Error")

}

    private val networkReceiver=object :BroadcastReceiver(){

        override fun onReceive(p0: Context?, p1: Intent?) {
            updateConnection()        }
    }
    private fun updateConnection(){

        val activeNetwork:NetworkInfo?=connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected==true)

    }
}