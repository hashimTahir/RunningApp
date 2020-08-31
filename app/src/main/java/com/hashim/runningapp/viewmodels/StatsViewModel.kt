/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.hashim.runningapp.repository.local.LocalRepo

class StatsViewModel @ViewModelInject constructor(
    private val hLocalRepo: LocalRepo
) : ViewModel() {
    val hTotalRunningTimeInMillis = hLocalRepo.hGetTotalRunningTimeInMillis()
    val hTotalDistance = hLocalRepo.hGetTotalDistance()
    val hTotalCaloriesBurnt = hLocalRepo.hGetTotalCaloriesBurnt()
    val hTotalRunsByAvgSpeed = hLocalRepo.hGetAverage()

    val hRunsSortedByDate = hLocalRepo.hGetAllRunsSortedByDate()
}