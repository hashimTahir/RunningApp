/*
 * Copyright (c) 2020/  8/ 26.  Created by Hashim Tahir
 */

package com.hashim.runningapp.utils

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import com.hashim.runningapp.services.PolyLine
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit

object TrackingUtils {
    fun hHasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        }

    fun hGetFormattedTime(time: Long, includeMillis: Boolean = false): String {
        var hTimeInMillis = time

        val hHours = TimeUnit.MILLISECONDS.toHours(hTimeInMillis)
        hTimeInMillis -= TimeUnit.HOURS.toMillis(hHours)

        val hMinutes = TimeUnit.MILLISECONDS.toMinutes(hTimeInMillis)
        hTimeInMillis -= TimeUnit.MINUTES.toMillis(hMinutes)

        val hSeconds = TimeUnit.MILLISECONDS.toSeconds(hTimeInMillis)

        if (!includeMillis) {
            return "${if (hHours < 10) "0" else ""}$hHours:" +
                    "${if (hMinutes < 10) "0" else ""}$hMinutes:" +
                    "${if (hSeconds < 10) "0" else ""}$hSeconds"

        }

        hTimeInMillis -= TimeUnit.SECONDS.toMillis(hSeconds)
        hTimeInMillis /= 10
        return "${if (hHours < 10) "0" else ""}$hHours:" +
                "${if (hMinutes < 10) "0" else ""}$hMinutes:" +
                "${if (hSeconds < 10) "0" else ""}$hSeconds:" +
                "${if (hTimeInMillis < 10) "0" else ""}$hTimeInMillis"
    }

    fun hCalculatePolyLineLength(polyLine: PolyLine): Float {
        var hDistance = 0F
        for (i in 0..polyLine.size - 2) {
            val hPosition1 = polyLine.get(i)
            val hPosition2 = polyLine.get(i + 1)

            /*Required by location.distance between, in it the result will be stored.*/
            val hResult = FloatArray(1)
            Location.distanceBetween(
                hPosition1.latitude,
                hPosition1.longitude,
                hPosition2.latitude,
                hPosition2.longitude,
                hResult
            )
            hDistance += hResult[0]
        }
        return hDistance
    }
}