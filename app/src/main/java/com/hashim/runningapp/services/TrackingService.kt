/*
 * Copyright (c) 2020/  8/ 26.  Created by Hashim Tahir
 */

package com.hashim.runningapp.services

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_PAUSE_SERVICE
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_START_OR_RESUME
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_STOP_SERVICE
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

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun getLifecycle(): Lifecycle {
        return super.getLifecycle()
    }
}