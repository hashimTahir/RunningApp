/*
 * Copyright (c) 2020/  8/ 26.  Created by Hashim Tahir
 */

package com.hashim.runningapp.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.hashim.runningapp.R
import com.hashim.runningapp.ui.MainActivity
import com.hashim.runningapp.utils.Constants
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_PAUSE_SERVICE
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_START_OR_RESUME
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_STOP_SERVICE
import com.hashim.runningapp.utils.NotificationUtils
import com.hashim.runningapp.utils.NotificationUtils.Companion.H_NOTIFICATION_ID
import timber.log.Timber

/*Just a name convention to use complex variables*/

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>

class TrackingService : LifecycleService() {
    var hIsFirstRun = true

    companion object {
        /*Observe these in the activity*/
        val hIsTrackingUserMLD = MutableLiveData<Boolean>()
        val hListOfCordinatesMLD = MutableLiveData<PolyLines>()
        val hIsTrackingUserLD = hIsTrackingUserMLD.value
        val hListOfCordinatesLD = hListOfCordinatesMLD.value
    }

    private fun hInitilizeMLD() {
        hIsTrackingUserMLD.value = false
        hListOfCordinatesMLD.value = mutableListOf()

    }

    private fun hAddEmptyPolyLine() {
        hListOfCordinatesMLD.value?.apply {
            add(mutableListOf())
            hListOfCordinatesMLD.postValue(this)
        } ?: hListOfCordinatesMLD.postValue(mutableListOf(mutableListOf()))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                H_ACTION_STOP_SERVICE -> {
                    Timber.d("H_ACTION_STOP_SERVICE")

                }
                H_ACTION_START_OR_RESUME -> {
                    Timber.d("H_ACTION_START_OR_RESUME")
                    if (hIsFirstRun) {
                        hStartForeGroundService()
                    } else {
                        Timber.d("Already Running")

                    }
                }
                H_ACTION_PAUSE_SERVICE -> {
                    Timber.d("H_ACTION_PAUSE_SERVICE")
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    /*This adds new lat lng at the end of the list*/
    private fun hAddLatLngToPolyline(location: Location?) {
        location?.let {
            val hLatLng = LatLng(location.latitude, location.longitude)
            hListOfCordinatesMLD.value?.apply {
                last().add(hLatLng)
                hListOfCordinatesMLD.postValue(this)
            }
        }

    }

    private fun hStartForeGroundService() {
        hAddEmptyPolyLine()
        var hNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils.hCreateNotificationChannel(hNotificationManager)
        }
        val hNotificationBuilder =
            NotificationCompat.Builder(this, NotificationUtils.H_NOTIFICATION_CHANNEL_ID)
        hNotificationBuilder
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("00:00:00")
            .setContentIntent(hGetPendingIntent())

        startForeground(
            H_NOTIFICATION_ID,
            hNotificationBuilder.build()
        )
    }

    private fun hGetPendingIntent(): PendingIntent {
        /*only update dont create a new every time*/
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java)
                .apply {
                    setAction(Constants.H_ACTION_SHOW_TRACKING_FRAGMENT)
                }, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}