/*
 * Copyright (c) 2020/  8/ 24.  Created by Hashim Tahir
 */

package com.hashim.runningapp.repository.local

interface Queries {
    companion object {
        const val H_RUNS_ALL_SORTED_BY_DATE =
            "SELECT * FROM RUNNINGTABLE ORDER BY hTimeInMills DESC"

        const val H_RUNS_ALL_SORTED_BY_TIME_IN_MILLS =
            "SELECT * FROM RUNNINGTABLE ORDER BY hTimeInMills DESC"

        const val H_RUNS_ALL_SORTED_BY_CALORIES_BURNT =
            "SELECT * FROM RUNNINGTABLE ORDER BY hCaloriesBurnt DESC"

        const val H_RUNS_ALL_SORTED_BY_DISTANCE_IN_METERS =
            "SELECT * FROM RUNNINGTABLE ORDER BY hDistanceInMeters DESC"

        const val H_RUNS_ALL_SORTED_BY_AVERAGE_SPEED =
            "SELECT * FROM RUNNINGTABLE ORDER BY hAverageSpeedInKms DESC"

        const val H_GET_TOTAL_RUNNING_TIME =
            "SELECT SUM(hTimeInMills) FROM RUNNINGTABLE"

        const val H_GET_TOTAL_CALORIES_BURNT =
            "SELECT SUM(hCaloriesBurnt) FROM RUNNINGTABLE"

        const val H_GET_TOTAL_DISTANCE =
            "SELECT SUM(hDistanceInMeters) FROM RUNNINGTABLE"

        const val H_GET_AVERAGE_SPEED =
            "SELECT AVG(hAverageSpeedInKms) FROM RUNNINGTABLE"


    }
}