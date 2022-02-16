package com.example.weatherapp.prsentation.weather.view.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.*
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentNotificationsBinding
import com.google.android.material.snackbar.Snackbar
import com.mcit.kmvvm.data.remote.LAt
import com.mcit.kmvvm.data.remote.NOTIFICATION
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.layout_date.*
import ru.ifr0z.notify.custom.TimePickerCustom
import ru.ifr0z.notify.work.NotifyWork
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class NotificationsFragment : Fragment() {

    //private  val viewModel: NotificationsViewModel by sharedViewModel()
    private lateinit var vm: NotificationsViewModel
    private   var notification: Int = 0
    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var adapterAlarm: AdapterAlarm

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        activity?.let {
            vm = ViewModelProvider(it).get(NotificationsViewModel::class.java)
        }


        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences(
            "sharedPrefFile",
            Context.MODE_PRIVATE
        )
          notification  = sharedPreferences.getInt(NOTIFICATION, -1)


        adapterAlarm = AdapterAlarm(ArrayList(),vm)
        binding?.rcNotification?.layoutManager =
            GridLayoutManager(context, 2)
        binding?.rcNotification?.adapter = adapterAlarm

        binding.btAddNotification.setOnClickListener {
            if(notification.equals(2)){
                Snackbar.make(container_notification, getString(R.string.notification_is_disable), Snackbar.LENGTH_LONG).show()

            }else{
            addAlarm()}
        }
        activity?.let { it1 ->
            vm.allAlarms.observe(it1, androidx.lifecycle.Observer {
                adapterAlarm.setAlarm(it as ArrayList<ModelAlarm>)
                adapterAlarm.notifyDataSetChanged()
                if(it.size>0){
                    binding?.iconNotification.visibility=View.GONE
                }else
                    binding?.iconNotification.visibility=View.VISIBLE

            })
        }


        return root
    }

    private fun addAlarm() {


        val dialog = DialogPlus.newDialog(activity)
            .setContentHolder(ViewHolder(R.layout.layout_date))
            .setExpanded(true, 1600)
            .create()
        dialog.show()
        val view = dialog.holderView
        val date_p = view.findViewById<DatePicker>(R.id.date_p)
        val time_p = view.findViewById<TimePickerCustom>(R.id.time_p)
        val containerDate = view.findViewById<ScrollView>(R.id.containerDate)
        val date_p_to = view.findViewById<DatePicker>(R.id.date_p_to)
        val time_p_to = view.findViewById<TimePickerCustom>(R.id.time_p_to)
        val btSaveAlarm = view.findViewById<Button>(R.id.btSaveAlarm)
        btSaveAlarm.setOnClickListener {


            val customCalendarStart = Calendar.getInstance()
            val customCalendarEnd = Calendar.getInstance()

            customCalendarStart.set(
                date_p.year, date_p.month, date_p.dayOfMonth, time_p.hour, time_p.minute, 0
            )
            customCalendarEnd.set(
                date_p_to.year,
                date_p_to.month,
                date_p_to.dayOfMonth,
                time_p_to.hour,
                time_p_to.minute,
                0
            )
            val customTimeStart = customCalendarStart.timeInMillis
            val currentTimeStart = System.currentTimeMillis()
            if (customTimeStart > currentTimeStart) {
                val dataStart = Data.Builder().putInt(NotifyWork.NOTIFICATION_ID, 0).build()
                val delayStart = customTimeStart - currentTimeStart

                val customTimeEnd = customCalendarStart.timeInMillis
                val currentTimeEnd = System.currentTimeMillis()


                val titleNotificationSchedule = getString(R.string.notification_schedule_title)
                val patternNotificationSchedule = getString(R.string.notification_schedule_pattern)
                val patternNotificationScheduleDate =
                    getString(R.string.notification_schedule_pattern_date)
                val patternNotificationScheduleTime =
                    getString(R.string.notification_schedule_pattern_time)

                Snackbar.make(
                    containerDate, titleNotificationSchedule + SimpleDateFormat(
                        patternNotificationSchedule, Locale.getDefault()
                    ).format(customCalendarStart.time).toString(),
                    Snackbar.LENGTH_LONG
                ).show()

                var date_start = SimpleDateFormat(
                    patternNotificationScheduleDate, Locale.getDefault()
                ).format(customCalendarStart.time).toString()
                var time_start = SimpleDateFormat(
                    patternNotificationScheduleTime, Locale.getDefault()
                ).format(customCalendarStart.time).toString()


                var date_end = SimpleDateFormat(
                    patternNotificationScheduleDate, Locale.getDefault()
                ).format(customCalendarEnd.time).toString()
                var time_end = SimpleDateFormat(
                    patternNotificationScheduleTime, Locale.getDefault()
                ).format(customCalendarEnd.time).toString()

                scheduleNotification(delayStart, dataStart, time_start)
                 //insert data in room
                vm.insert(ModelAlarm(date_start,date_end,time_start,time_end))

                activity?.let { it1 ->
                    vm.allAlarms.observe(it1, androidx.lifecycle.Observer {
                        adapterAlarm.setAlarm(it as ArrayList<ModelAlarm>)
                        adapterAlarm.notifyDataSetChanged()
                        if(it.size>0){
                            binding?.iconNotification.visibility=View.GONE
                        }else
                            binding?.iconNotification.visibility=View.VISIBLE

                    })
                }


                Log.d("addAlarm", "addAlarm: 1" + date_end)
                Log.d("addAlarm", "addAlarm: 2" + time_end)

            } else {
                val errorNotificationSchedule = getString(R.string.notification_schedule_error)
                Snackbar.make(containerDate, errorNotificationSchedule, Snackbar.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }
    }


    private fun scheduleNotification(delay: Long, data: Data, keyOfTrip: String) {
        val notificationWork = PeriodicWorkRequestBuilder<NotifyWork>(15, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).addTag(keyOfTrip)
            .build()

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        // Set Execution around 05:00:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, 10)
        dueDate.set(Calendar.MINUTE, 25)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequestBuilder<NotifyWork>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .setInputData(data).addTag(keyOfTrip)
            .build()



        val instanceWorkManager = activity?.let { WorkManager.getInstance(it) }
        instanceWorkManager
            ?.enqueue(notificationWork)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}