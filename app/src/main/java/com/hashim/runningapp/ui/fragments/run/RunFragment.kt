/*
 * Copyright (c) 2020/  8/ 30.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments.run

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hashim.runningapp.R
import com.hashim.runningapp.ui.fragments.BaseFragment
import com.hashim.runningapp.utils.Constants
import com.hashim.runningapp.utils.SortType
import com.hashim.runningapp.utils.TrackingUtils
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class RunFragment : BaseFragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {
    private lateinit var hRunAdapter: RunAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hRequestPermissions()
        hSetupListeners()
        hInitRecyclerView()
        hSetupSortType()
        hSubscribeObservers()

    }

    private fun hSetupSortType() {
        when (hMainViewModel.hSortType) {
            SortType.H_RUNNING_TIME -> spFilter.setSelection(1)
            SortType.H_AVG_SPEED -> spFilter.setSelection(3)
            SortType.H_CALORIES_BURNED -> spFilter.setSelection(4)
            SortType.H_DATE -> spFilter.setSelection(0)
            SortType.H_DISTANCE -> spFilter.setSelection(2)
        }
    }

    private fun hSubscribeObservers() {
        hMainViewModel.hRunsMediatorLiveData
            .observe(
                viewLifecycleOwner, {
                    hRunAdapter.hSubmitList(it)
                }
            )
    }

    private fun hInitRecyclerView() {
        rvRuns.apply {
            hRunAdapter = RunAdapter()
            adapter = hRunAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun hSetupListeners() {
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_hRunFragment_to_hTrackingFragment)
        }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> hMainViewModel.hChangeSortType(SortType.H_DATE)
                    1 -> hMainViewModel.hChangeSortType(SortType.H_RUNNING_TIME)
                    2 -> hMainViewModel.hChangeSortType(SortType.H_DISTANCE)
                    3 -> hMainViewModel.hChangeSortType(SortType.H_AVG_SPEED)
                    4 -> hMainViewModel.hChangeSortType(SortType.H_CALORIES_BURNED)
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun hRequestPermissions() {
        if (TrackingUtils.hHasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept the location permissions",
                Constants.H_LOACTION_RC,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept the location permissions",
                Constants.H_LOACTION_RC,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,

                )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(
        requestCode: Int,
        perms: MutableList<String>
    ) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            hRequestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            Constants.H_LOACTION_RC,
            permissions,
            grantResults,
            this
        )
    }
}