/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.hashim.runningapp.R
import com.hashim.runningapp.utils.CustomMarkerView
import com.hashim.runningapp.utils.TrackingUtils
import com.hashim.runningapp.viewmodels.StatsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlin.math.round


@AndroidEntryPoint
class StatsFragment : Fragment(R.layout.fragment_stats) {
    private val hStatsViewModel: StatsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hSubscribeObservers()
        hSetupBarChart()
    }


    private fun hSetupBarChart() {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    private fun hSubscribeObservers() {
        hStatsViewModel.hTotalRunningTimeInMillis
            .observe(viewLifecycleOwner) {
                it?.let {
                    var hTotalTimeRun = TrackingUtils.hGetFormattedTime(it)
                    tvTotalTime.text = hTotalTimeRun
                }
            }

        hStatsViewModel.hTotalDistance
            .observe(viewLifecycleOwner) {
                it?.let {
                    var hKm = it / 1000F
                    val hTotalDistance = round(hKm * 10F) / 10F
                    val hTotalDistanceString = "$hTotalDistance km"
                    tvTotalDistance.text = hTotalDistanceString
                }
            }

        hStatsViewModel.hTotalRunsByAvgSpeed
            .observe(viewLifecycleOwner) {
                it?.let {
                    val hAvgSpeed = round(it * 10F) / 10F
                    val hAvgSpeedString = "$hAvgSpeed km/h"
                    tvAverageSpeed.text = hAvgSpeedString
                }
            }
        hStatsViewModel.hTotalCaloriesBurnt
            .observe(viewLifecycleOwner) {
                it?.let {
                    val hTotalCaloreies = "$it Calories"
                    tvTotalCalories.text = hTotalCaloreies
                }
            }

        hStatsViewModel.hRunsSortedByDate
            .observe(viewLifecycleOwner) {
                it?.let {
                    val hAllAvgSpeed = it.indices.map { i ->
                        BarEntry(i.toFloat(), it.get(i).hAverageSpeedInKms)
                    }
                    val hBarDataSet = BarDataSet(hAllAvgSpeed, "Avg Speed over Time")
                        .apply {
                            valueTextColor = Color.WHITE
                            color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                        }
                    barChart.data = BarData(hBarDataSet)
                    barChart.marker =
                        CustomMarkerView(
                            it.reversed(),
                            requireContext(),
                            R.layout.marker_view
                        )
                    barChart.invalidate()
                }
            }
    }


}