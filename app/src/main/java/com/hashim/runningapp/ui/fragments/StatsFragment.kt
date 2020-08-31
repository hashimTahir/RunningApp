/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hashim.runningapp.R
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
    }


}