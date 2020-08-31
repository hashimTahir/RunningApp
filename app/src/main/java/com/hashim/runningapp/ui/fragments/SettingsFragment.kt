/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.hashim.runningapp.R
import com.hashim.runningapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    @Inject
    lateinit var hSharedPreferences: SharedPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hLoadPrefsData()
        hSetListeners()
    }

    private fun hSetListeners() {
        if (hSaveChangesToPrefs()) {
            Snackbar.make(
                requireView(),
                "Chages saved",
                Snackbar.LENGTH_SHORT
            )
                .show()
        } else {
            Snackbar.make(
                requireView(),
                "Please fill out all details",
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun hLoadPrefsData() {
        val hName = hSharedPreferences.getString(Constants.H_KEY_NAME, "")
        val hWeight = hSharedPreferences.getFloat(Constants.H_KEY_WEIGHT, 80F)

        etName.setText(hName)
        etWeight.setText(hWeight.toString())
    }

    private fun hSaveChangesToPrefs(): Boolean {
        val hName = etName.text.toString()
        val hWeight = etWeight.text.toString()

        if (hName.isEmpty() || hWeight.isEmpty()) {
            return false
        }

        hSharedPreferences.edit()
            .putString(Constants.H_KEY_NAME, hName)
            .putFloat(Constants.H_KEY_WEIGHT, hWeight.toFloat())
            .apply()

        val hToolbarText = "Lets go $hName"
        requireActivity().tvToolbarTitle.text = hToolbarText
        return true
    }

}