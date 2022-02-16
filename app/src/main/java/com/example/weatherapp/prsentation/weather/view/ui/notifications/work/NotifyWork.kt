package ru.ifr0z.notify.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result.success
import androidx.work.WorkerParameters
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
 import com.mcit.kmvvm.data.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ifr0z.notify.extension.vectorToBitmap

class NotifyWork(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences(
            "sharedPrefFile",
            Context.MODE_PRIVATE
        )
        val lat: Double = sharedPreferences.getFloat(LAt, -1f).toDouble()
        val long: Double = sharedPreferences.getFloat(LONG, -1f).toDouble()
        val temp = sharedPreferences.getString(TEMP, "")
        val wind = sharedPreferences.getString(WIND, "")
        val language = sharedPreferences.getString(LANGUAGE, "")


        GlobalScope.launch(Dispatchers.IO) {
            val response =
                temp?.let {
                    language?.let { it1 ->
                        RetrofitClient.getApiService().getWeatherDetails(lat, long, it, it1,
                            API_KEY
                        )
                    }
                }

            withContext(Dispatchers.Main){
                if (response != null) {
                    if (response.isSuccessful) {
                        response?.body()?.let {
                            Log.d("WeatherReporrr", "onResponserrrr: ${it}")
                             val id = inputData.getLong(NOTIFICATION_ID, 0).toInt()
                            if(it.alerts.isNullOrEmpty()){
                                if(language.equals("ar")) {

                                    sendNotification(id, context.getString(R.string.massage_notifications_ar))
                                }else{
                                    sendNotification(id, context.getString(R.string.massage_notifications))

                                }
                            }else
                            sendNotification(id,it.alerts.get(0).event)

                        }
                    }
                }
            }

        }


        return success()
    }

    private fun sendNotification(id: Int,message:String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences(
            "sharedPrefFile",
            Context.MODE_PRIVATE
        )

        val language = sharedPreferences.getString(LANGUAGE, "")
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.app_icon)
        var title=""
        if(language.equals("ar")){
            title=context.getString(R.string.alert_weather_ar)
        }else{
            title=context.getString(R.string.alert_weather)

        }
        val titleNotification = title

        val subtitleNotification =message
        val pendingIntent = getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.app_icon)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }
}