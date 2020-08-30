/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hashim.runningapp.R
import com.hashim.runningapp.db.Run
import com.hashim.runningapp.services.PolyLine
import com.hashim.runningapp.services.TrackingService
import com.hashim.runningapp.utils.Constants
import com.hashim.runningapp.utils.TrackingUtils
import kotlinx.android.synthetic.main.fragment_tracking.*
import java.util.*
import kotlin.math.round


class TrackingFragment : BaseFragment(R.layout.fragment_tracking) {
    private var hGoogleMap: GoogleMap? = null
    private var hIsTracking = false
    private var hPathPoints = mutableListOf<PolyLine>()
    private var hCurrentTimeInMills = 0L

    private var hMenu: Menu? = null
    private var hWeight = 80f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hSetupListeners()

        setHasOptionsMenu(true)

        mapView?.onCreate(savedInstanceState)
        mapView.getMapAsync {
            hGoogleMap = it
            hAddAllPolyLines()
        }
        hSubscribeToServiceObservers()
    }

    /*Connect last 2 points of pollyline list*/
    private fun hDrawLatestPolyLine() {
        if (hPathPoints.isNotEmpty() && hPathPoints.last().size > 1) {
            val hPreLastLatLng = hPathPoints.last()[hPathPoints.last().size - 2]
            val hLastLatLng = hPathPoints.last().last()
            val hPolylineOptions = PolylineOptions()
                .color(Constants.H_POLYLINE_COLOR)
                .width(Constants.H_POLYLINE_WIDTH)
                .add(hPreLastLatLng)
                .add(hLastLatLng)
            hGoogleMap?.addPolyline(hPolylineOptions)
        }

    }

    private fun hSubscribeToServiceObservers() {
        TrackingService.hIsTrackingUserLD.observe(
            viewLifecycleOwner, {
                hUpdateTracking(it)

            }
        )
        TrackingService.hListOfCordinatesLD.observe(
            viewLifecycleOwner, {
                hPathPoints = it
                hDrawLatestPolyLine()
                hMoveCameraToUser()
            }
        )

        TrackingService.hRunningTimeInMillisLD.observe(viewLifecycleOwner, {
            hCurrentTimeInMills = it
            val hFormatedTime = TrackingUtils.hGetFormattedTime(hCurrentTimeInMills, true)
            tvTimer.text = hFormatedTime

        })
    }

    /*Turn service on and off*/
    private fun hToggleRun() {
        if (hIsTracking) {
            hMenu?.get(0)?.isVisible = true
            hSendCommandsToServie(Constants.H_ACTION_PAUSE_SERVICE)
        } else {
            hSendCommandsToServie(Constants.H_ACTION_START_OR_RESUME)
        }
    }

    /*Observe the values from the server and update the uI*/

    private fun hUpdateTracking(isTracking: Boolean) {
        this.hIsTracking = isTracking
        if (!isTracking) {
            btnToggleRun.text = "Start"
            btnFinishRun.visibility = View.VISIBLE
        } else {
            btnToggleRun.text = "Stop"
            hMenu?.get(0)?.isVisible = true
            btnFinishRun.visibility = View.GONE
        }

    }

    private fun hEndRunAndSaveToDb() {
        hGoogleMap?.snapshot { hMapBitmap ->
            var hDistanceInMeters = 0
            for (polyline in hPathPoints) {
                hDistanceInMeters += TrackingUtils.hCalculatePolyLineLength(
                    polyline
                )
                    .toInt()
            }
            val hAverageSpeed = round(
                (hDistanceInMeters / 1000F) /
                        (hCurrentTimeInMills / 1000F / 60 / 60) * 10
            ) / 10F

            val hDateTimeStamp = Calendar.getInstance().timeInMillis
            val hCaloriesBurned = ((hDistanceInMeters / 1000F) * hWeight).toInt()
            val hRun = Run(
                hMapBitmap,
                hDateTimeStamp,
                hAverageSpeed,
                hDistanceInMeters,
                hCurrentTimeInMills,
                hCaloriesBurned
            )
        }

    }

    /*Zoom out so the image can be taken required for storing in db
    * LatLang bounds does the same thing in google maps*/
    private fun hZoomOutToTrackTheWholeRun() {
        val hLatLngBound = LatLngBounds.builder()
        for (polyLine in hPathPoints) {
            for (point in polyLine) {
                hLatLngBound.include(point)
            }
        }
        hGoogleMap?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                hLatLngBound.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05F).toInt()
            )
        )

    }

    private fun hMoveCameraToUser() {
        if (hPathPoints.isNotEmpty() && hPathPoints.last().isNotEmpty()) {
            hGoogleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    hPathPoints.last().last(),
                    Constants.H_CAMERA_ZOOM
                )
            )
        }
    }

    /*When activity/fragment is recreated re draw the path.*/
    private fun hAddAllPolyLines() {
        for (polyLine in hPathPoints) {

            val hPolylineOptions = PolylineOptions()
                .color(Constants.H_POLYLINE_COLOR)
                .width(Constants.H_POLYLINE_WIDTH)
                .addAll(polyLine)

            hGoogleMap?.addPolyline(hPolylineOptions)
        }

    }

    private fun hSetupListeners() {
        btnToggleRun.setOnClickListener {
            hToggleRun()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)

    }

    private fun hSendCommandsToServie(action: String): Intent {
        return Intent(
            requireContext(),
            TrackingService::class.java
        ).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu, menu)
        hMenu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.hCancelTracking -> hShowAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (hCurrentTimeInMills > 0) {
            hMenu?.get(0)?.isVisible = true
        }
    }

    private fun hShowAlertDialog() {
        val hDialog = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.AlertDialogTheme
        )
            .setTitle("Cancel the run")
            .setMessage("Are you sure to cancel the current run")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("yes") { _, _ ->
                hStopRun()
            }
            .setNegativeButton("No") { dialoginterface, _ ->
                dialoginterface.cancel()

            }
            .create()
            .show()
    }

    private fun hStopRun() {
        hSendCommandsToServie(Constants.H_ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_hTrackingFragment_to_hRunFragment)
    }
}