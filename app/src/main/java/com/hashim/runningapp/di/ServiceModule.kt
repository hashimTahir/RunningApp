/*
 * Copyright (c) 2020/  8/ 29.  Created by Hashim Tahir
 */

package com.hashim.runningapp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.hashim.runningapp.R
import com.hashim.runningapp.ui.MainActivity
import com.hashim.runningapp.utils.Constants
import com.hashim.runningapp.utils.NotificationUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    @Provides
    @ServiceScoped
    fun hProvidesFusedLocationProviderClient(
        @ApplicationContext app: Context
    ): FusedLocationProviderClient {
        return FusedLocationProviderClient(app)
    }

    @ServiceScoped
    @Provides
    fun hProvidesPendingIntent(
        @ApplicationContext app: Context
    ): PendingIntent {
        return PendingIntent.getActivity(
            app,
            0,
            Intent(app, MainActivity::class.java)
                .apply {
                    setAction(Constants.H_ACTION_SHOW_TRACKING_FRAGMENT)
                }, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @ServiceScoped
    @Provides
    fun hProvidesNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(
            app,
            NotificationUtils.H_NOTIFICATION_CHANNEL_ID
        )
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_run)
            .setContentTitle(app.getString(R.string.app_name))
            .setContentText("00:00:00")
            .setContentIntent(pendingIntent)
    }
}