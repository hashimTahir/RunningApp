/*
 * Copyright (c) 2020/  8/ 26.  Created by Hashim Tahir
 */

package com.hashim.runningapp.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.hashim.runningapp.R
import com.hashim.runningapp.utils.Constants
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_PAUSE_SERVICE
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_START_OR_RESUME
import com.hashim.runningapp.utils.Constants.Companion.H_ACTION_STOP_SERVICE
import com.hashim.runningapp.utils.NotificationUtils
import com.hashim.runningapp.utils.NotificationUtils.Companion.H_NOTIFICATION_ID
import com.hashim.runningapp.utils.TrackingUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/*Just a name convention to use complex variables*/

typealias PolyLine = MutableList<LatLng>
typealias PolyLines = MutableList<PolyLine>


@AndroidEntryPoint
class TrackingService : LifecycleService() {
    var hIsFirstRun = true
    var hKillService = false

    @Inject
    lateinit var hFusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var hNotificationBuilder: NotificationCompat.Builder

    /*To Updated the already showing notification, using the same id and updaing the content*/
    lateinit var hCurrentNotification: NotificationCompat.Builder

    private val hRunningTimeinSecondsMLD = MutableLiveData<Long>()

    companion object {
        /*Observe these in the activity*/
        private val hIsTrackingUserMLD = MutableLiveData<Boolean>()
        private val hListOfCordinatesMLD = MutableLiveData<PolyLines>()
        private val hRunningTimeInMillisMLD = MutableLiveData<Long>()
        val hIsTrackingUserLD: LiveData<Boolean>
            get() {
                return hIsTrackingUserMLD
            }
        val hListOfCordinatesLD: LiveData<PolyLines>
            get() {
                return hListOfCordinatesMLD
            }
        val hRunningTimeInMillisLD: LiveData<Long>
            get() {
                return hRunningTimeInMillisMLD
            }


    }


    /*Get continous location updates Annonymous callback defined globally*/
    val hLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            if (hIsTrackingUserMLD.value!!) {
                locationResult?.locations?.let { locationsList ->
                    for (location in locationsList) {
                        hAddLatLngToPolyline(location)
                    }
                }
            }
        }
    }


    private var hIsTimerEnabled = false
    private var hLapTime = 0L
    private var hTotalTime = 0L
    private var hTimeStarted = 0L
    private var hLastSecondTimeStamp = 0L
    private fun hStartTimer() {
        hAddEmptyPolyLine()
        hIsTrackingUserMLD.value = true
        hTimeStarted = System.currentTimeMillis()
        hIsTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (hIsTrackingUserLD.value!!) {
                /*Time difference between now and time started.*/
                hLapTime = System.currentTimeMillis() - hTimeStarted


                /*Used for updating fragment timer*/
                hRunningTimeInMillisMLD.postValue(hTotalTime + hLapTime)

                /*Used for updating notificaiton*/
                if (hRunningTimeInMillisLD.value!! >= hLastSecondTimeStamp + 1000L) {
                    hRunningTimeinSecondsMLD.postValue(hRunningTimeinSecondsMLD.value!! + 1)
                    hLastSecondTimeStamp += 1000L
                }
                delay(Constants.H_DELAY_INTERVAL)
            }
            hTotalTime += hLapTime

        }
    }


    /*Initilize the MLD*/
    private fun hInitilizeMLD() {
        hIsTrackingUserMLD.value = false
        hListOfCordinatesMLD.value = mutableListOf()
        hRunningTimeinSecondsMLD.value = 0L
        hRunningTimeInMillisMLD.value = 0L

    }

    /*Add empty list if the location is stoped/ initilized */
    private fun hAddEmptyPolyLine() {
        hListOfCordinatesMLD.value?.apply {
            add(mutableListOf())
            hListOfCordinatesMLD.postValue(this)
        } ?: hListOfCordinatesMLD.postValue(mutableListOf(mutableListOf()))
    }

    private fun hUpdateNotificaiton(istracking: Boolean) {
        val hNotificationText = if (istracking) "Pause" else "Resume"
        val hPendingIntent = if (istracking) {
            val hPauseIntent = Intent(this, TrackingService::class.java)
                .apply {
                    setAction(Constants.H_ACTION_PAUSE_SERVICE)
                }
            PendingIntent.getService(
                this,
                1,
                hPauseIntent,
                FLAG_UPDATE_CURRENT
            )
        } else {
            val hResumeIntent = Intent(this, TrackingService::class.java)
                .apply {
                    setAction(Constants.H_ACTION_START_OR_RESUME)
                }
            PendingIntent.getService(
                this,
                2,
                hResumeIntent,
                FLAG_UPDATE_CURRENT
            )
        }
        val hNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        hCurrentNotification.javaClass.getDeclaredField("mActions")
            .apply {
                isAccessible = true
                set(hCurrentNotification, ArrayList<NotificationCompat.Action>())
            }
        if (!hKillService) {
            hCurrentNotification = hNotificationBuilder
                .addAction(R.drawable.ic_add_black, hNotificationText, hPendingIntent)

            hNotificationManager.notify(H_NOTIFICATION_ID, hCurrentNotification.build())
        }


    }

    private fun hKillService() {
        hKillService = true
        hIsFirstRun = true
        hPauseService()
        hInitilizeMLD()
        stopForeground(true)
        stopSelf()
    }

    /*Start or stop the location tracking*/
    @SuppressLint("MissingPermission")
    private fun hUpdateTracking(istracking: Boolean) {
        if (istracking) {
            if (TrackingUtils.hHasLocationPermissions(this)) {
                var hLocationRequest = LocationRequest().apply {
                    interval = Constants.H_LOCATION_UPDATE_INTERVAL
                    fastestInterval = Constants.H_FATEST_LOCATION_UPDATE_INTERVAL
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                hFusedLocationProviderClient.requestLocationUpdates(
                    hLocationRequest,
                    hLocationCallback,
                    Looper.getMainLooper()
                )
            } else {
                hFusedLocationProviderClient.removeLocationUpdates(
                    hLocationCallback
                )
            }
        }
    }

    /*Initilize the fusedLocation provider client*/
    override fun onCreate() {
        super.onCreate()
        hInitilizeMLD()
        hIsTrackingUserLD.observe(this, Observer {
            hUpdateTracking(it)
            hUpdateNotificaiton(it)
        })

        hCurrentNotification = hNotificationBuilder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                H_ACTION_STOP_SERVICE -> {
                    Timber.d("H_ACTION_STOP_SERVICE")
                    hKillService()
                }
                H_ACTION_START_OR_RESUME -> {
                    Timber.d("H_ACTION_START_OR_RESUME")
                    if (hIsFirstRun) {
                        hStartForeGroundService()
                    } else {
                        hStartTimer()
                        Timber.d("Already Running")

                    }
                }
                H_ACTION_PAUSE_SERVICE -> {
                    Timber.d("H_ACTION_PAUSE_SERVICE")
                    hPauseService()
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

    /*Starts the serveice and add foreground notification */
    private fun hStartForeGroundService() {
        hStartTimer()
        hIsTrackingUserMLD.value = true
        var hNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils.hCreateNotificationChannel(hNotificationManager)
        }

        startForeground(
            H_NOTIFICATION_ID,
            hNotificationBuilder.build()
        )
        hRunningTimeinSecondsMLD.observe(this, {
            if (!hKillService) {
                val hNotifcation = hCurrentNotification
                    .setContentText(TrackingUtils.hGetFormattedTime(it * 1000L))

                Timber.d("hRunning TimeINSeconds Mld %s", it)
                hNotificationManager.notify(H_NOTIFICATION_ID, hNotifcation.build())
            }
        })
    }

    private fun hPauseService() {
        hIsTrackingUserMLD.postValue(false)
        hIsTimerEnabled = false
    }


}