/*
 * Copyright (c) 2020/  8/ 24.  Created by Hashim Tahir
 */

package com.hashim.runningapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "RunningTable"
)
data class Run(
    var hPreviewImage: Bitmap? = null,
    var hTimStamp: Long = 0L,
    var hAverageSpeedInKms: Float = 0F,
    var hDistanceInMeters: Int = 0,
    var hTimeInMills: Long = 0L,
    var hCaloriesBurnt: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var hId: Int? = null
}