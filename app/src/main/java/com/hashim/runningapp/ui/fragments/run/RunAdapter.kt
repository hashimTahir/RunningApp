/*
 * Copyright (c) 2020/  8/ 30.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments.run

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hashim.runningapp.R
import com.hashim.runningapp.db.Run
import com.hashim.runningapp.utils.TrackingUtils
import kotlinx.android.synthetic.main.item_run.view.*
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    val hDiffCallBack = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hId == newItem.hId
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    val hListDiffer = AsyncListDiffer(this, hDiffCallBack)

    fun hSubmitList(list: List<Run>) {
        hListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_run,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val hRun = hListDiffer.currentList.get(position)
        holder.itemView.apply {
            Glide.with(this)
                .load(hRun.hPreviewImage)
                .into(ivRunImage)
            val hCalendar = Calendar.getInstance()
                .apply {
                    timeInMillis = hRun.hTimStamp
                }
            val hDateFormat = SimpleDateFormat(
                "dd.MM.yy",
                Locale.getDefault()
            )
            tvDate.text = hDateFormat.format(hCalendar.time)

            val hAvgSpeed = "${hRun.hAverageSpeedInKms} km/h"
            tvAvgSpeed.text = hAvgSpeed

            val hDistanceInKm = "${hRun.hDistanceInMeters / 1000F}Km"
            tvDistance.text = hDistanceInKm

            tvTime.text = TrackingUtils.hGetFormattedTime(hRun.hTimeInMills)

            val hCaloriesBurned = "${hRun.hCaloriesBurnt}Kcal"
            tvCalories.text = hCaloriesBurned
        }
    }

    override fun getItemCount(): Int {
        return hListDiffer.currentList.size
    }

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}