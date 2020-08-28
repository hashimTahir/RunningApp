/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        }
    }

    /*Connect last 2 points of pollyline list*/
    private fun hDrawPolyLine() {
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

    private fun hSetupListeners() {
        btnToggleRun.setOnClickListener {
            hSendCommandsToServie(Constants.H_ACTION_START_OR_RESUME)
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