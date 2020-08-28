/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.hashim.runningapp.R
import com.hashim.runningapp.services.PolyLine
import com.hashim.runningapp.services.TrackingService
import com.hashim.runningapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_tracking.*


class TrackingFragment : BaseFragment(R.layout.fragment_tracking) {
    private var hGoogleMap: GoogleMap? = null
    private var hIsTracking = false
    private var hPathPoints = mutableListOf<PolyLine>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hSetupListeners()
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
    }

    /*Turn service on and off*/
    private fun hToggleRun() {
        if (hIsTracking) {
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
            btnFinishRun.visibility = View.GONE
        }

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

}