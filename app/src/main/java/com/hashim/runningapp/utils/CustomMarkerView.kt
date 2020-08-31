/*
 * Copyright (c) 2020/  8/ 31.  Created by Hashim Tahir
 */

package com.hashim.runningapp.utils

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.hashim.runningapp.db.Run
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    val hRuns: List<Run>,
    context: Context,
    layoutid: Int
) : MarkerView(context, layoutid) {
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val hCurrentRunId = e.x.toInt()
        val hRun = hRuns.get(hCurrentRunId)

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

        tvDuration.text = TrackingUtils.hGetFormattedTime(hRun.hTimeInMills)

        val hCaloriesBurned = "${hRun.hCaloriesBurnt}Kcal"
        tvCaloriesBurned.text = hCaloriesBurned
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }
}