/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.repository.local

import androidx.lifecycle.LiveData
import com.hashim.runningapp.db.Run
import com.hashim.runningapp.db.RunDao
import javax.inject.Inject

class LocalRepo @Inject constructor(
    val hRunDao: RunDao
) {
    suspend fun hInsertRun(run: Run) {
        hRunDao.hInsertRunItem(run)
    }


    suspend fun hDeleteRunItem(run: Run) {
        hRunDao.hDeleteRunItem(run)
    }

    fun hGetAllRunsSortedByDate(): LiveData<List<Run>> {
        return hRunDao.hGetAllRunsSortedByDate()
    }

    fun hGetAllRunsSortedByTimeInMills(): LiveData<List<Run>> {
        return hRunDao.hGetAllRunsSortedByTimeInMills()
    }

    fun hGetAllRunsSortedByCaloriesBurnt(): LiveData<List<Run>> {
        return hRunDao.hGetAllRunsSortedByCaloriesBurnt()

    }

    fun hGetAllRunsSortedByDistanceInMeters(): LiveData<List<Run>> {
        return hRunDao.hGetAllRunsSortedByDistanceInMeters()
    }

    fun hGetAllRunsSortedByAverageSpeed(): LiveData<List<Run>> {
        return hRunDao.hGetAllRunsSortedByAverageSpeed()
    }

    fun hGetTotalRunningTimeInMillis(): LiveData<Long> {
        return hRunDao.hGetTotalRunningTimeInMillis()
    }

    fun hGetTotalCaloriesBurnt(): LiveData<Int> {
        return hRunDao.hGetTotalCaloriesBurnt()
    }

    fun hGetTotalDistance(): LiveData<Int> {
        return hRunDao.hGetTotalDistance()
    }

    fun hGetAverage(): LiveData<Float> {
        return hRunDao.hGetAverage()
    }

}