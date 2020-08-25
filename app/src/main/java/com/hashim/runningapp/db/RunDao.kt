/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun hInsertRunItem(run: Run)

    @Delete
    suspend fun hDeleteRunItem(run: Run)

    /*Live data dosent work with coroutine*/
    @Query(Queries.H_RUNS_ALL_SORTED_BY_DATE)
    fun hGetAllRunsSortedByDate(): LiveData<List<Run>>

    @Query(Queries.H_RUNS_ALL_SORTED_BY_TIME_IN_MILLS)
    fun hGetAllRunsSortedByTimeInMills(): LiveData<List<Run>>

    @Query(Queries.H_RUNS_ALL_SORTED_BY_CALORIES_BURNT)
    fun hGetAllRunsSortedByCaloriesBurnt(): LiveData<List<Run>>

    @Query(Queries.H_RUNS_ALL_SORTED_BY_DISTANCE_IN_METERS)
    fun hGetAllRunsSortedByDistanceInMeters(): LiveData<List<Run>>

    @Query(Queries.H_RUNS_ALL_SORTED_BY_AVERAGE_SPEED)
    fun hGetAllRunsSortedByAverageSpeed(): LiveData<List<Run>>

    @Query(Queries.H_GET_TOTAL_RUNNING_TIME)
    fun hGetTotalRunningTimeInMillis(): LiveData<Long>

    @Query(Queries.H_GET_TOTAL_CALORIES_BURNT)
    fun hGetTotalCaloriesBurnt(): LiveData<Int>

    @Query(Queries.H_GET_TOTAL_DISTANCE)
    fun hGetTotalDistance(): LiveData<Int>

    @Query(Queries.H_GET_AVERAGE_SPEED)
    fun hGetAverage(): LiveData<Float>
}