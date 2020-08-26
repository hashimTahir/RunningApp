/*
 * Copyright (c) 2020/  8/ 26.  Created by Hashim Tahir
 */

package com.hashim.runningapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationUtils {
    companion object {
        private const val H_NOTIFICATION_CHANNEL_ID = "H_NOTIFICATION_CHANNEL_ID"
        private const val H_NOTIFICATION_CHANNEL_NAME = "Tracking"
        private const val H_NOTIFICATION_ID = 1


        @RequiresApi(Build.VERSION_CODES.O)
        fun hCreateNotificationChannel(notificationManager: NotificationManager) {
            val hChannel = NotificationChannel(
                H_NOTIFICATION_CHANNEL_ID,
                H_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(hChannel)

        }
    }
}