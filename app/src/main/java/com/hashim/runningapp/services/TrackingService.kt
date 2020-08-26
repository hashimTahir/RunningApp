/*
 * Copyright (c) 2020/  8/ 26.  Created by Hashim Tahir
 */

package com.hashim.runningapp.services

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.hashim.runningapp.R
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_PAUSE_SERVICE
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_START_OR_RESUME
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_STOP_SERVICE
import com.hashim.runningapp.utils.NotificationUtils
import timber.log.Timber

class TrackingService : LifecycleService() {
    override fun onCreate() {
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                H_ACTION_STOP_SERVICE -> {
                    Timber.d("H_ACTION_STOP_SERVICE")
                }
                H_ACTION_START_OR_RESUME -> {
                    Timber.d("H_ACTION_START_OR_RESUME")
                }
                H_ACTION_PAUSE_SERVICE -> {
                    Timber.d("H_ACTION_PAUSE_SERVICE")
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun hStartForeGroundService() {
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

    }
}