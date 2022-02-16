package com.example.weatherapp.prsentation.weather.view.ui.notifications

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.weatherapp.R


class AdapterAlarm(var listAlarm:ArrayList<ModelAlarm>,var viewModel: NotificationsViewModel): RecyclerView.Adapter<AdapterAlarm.ViewHolder>()  {
    inner class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val tvTimeStart:TextView
        get() = view.findViewById(R.id.tvTimeStart)
        val tvTimeEnd:TextView
            get() = view.findViewById(R.id.tvTimeEnd)
        val tvDateStart:TextView
            get() = view.findViewById(R.id.tvDateStart)
        val tvDateEnd:TextView
            get() = view.findViewById(R.id.tvDateEnd)
        val ivDeleteAlarm:ImageView
            get() = view.findViewById(R.id.ivDeleteAlarm)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viwe:View= LayoutInflater.from(parent.context).inflate(R.layout.item_alarm,parent,false)
        return ViewHolder(viwe)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvTimeStart.text=listAlarm.get(position).timeStart
        holder.tvTimeEnd.text=listAlarm.get(position).timeEnd
        holder.tvDateStart.text=listAlarm.get(position).dateStart
        holder.tvDateEnd.text=listAlarm.get(position).dateEnd
        holder.ivDeleteAlarm.setOnClickListener {

            val builder = AlertDialog.Builder(holder.ivDeleteAlarm.getContext())
            builder.setTitle(holder.ivDeleteAlarm.getContext().getString(R.string.are_you_sure))
            builder.setMessage(holder.ivDeleteAlarm.getContext().getString(R.string.delete_data))
            builder.setPositiveButton(
                holder.ivDeleteAlarm.getContext().getString(R.string.delete)
            ) { dialog: DialogInterface?, which: Int ->

            val workManager = WorkManager.getInstance()
                workManager.cancelAllWorkByTag(listAlarm.get(position).timeStart)
                viewModel.deleteAlarm(listAlarm.get(position))
                listAlarm.removeAt(position)
                notifyDataSetChanged()

            }
            builder.setNegativeButton(
                holder.ivDeleteAlarm.getContext().getString(R.string.cancel)
            ) { dialog: DialogInterface?, which: Int ->
                Toast.makeText(
                    holder.ivDeleteAlarm.getContext(),
                    holder.ivDeleteAlarm.getContext().getString(R.string.canclled),
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()



        }

    }

    fun setAlarm(alarm: ArrayList<ModelAlarm>) {
        this.listAlarm = alarm
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int =listAlarm.size
}