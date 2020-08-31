/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hashim.runningapp.R
import com.hashim.runningapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject


@AndroidEntryPoint
class SetupFragment : BaseFragment(R.layout.fragment_setup) {

    @Inject
    lateinit var hSharedPreferences: SharedPreferences


    @set:Inject
    var hIsFirstRun = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*Remove the setup fragment and move to the run fragment
        * */
        if (!hIsFirstRun) {
            val hNavOptions = NavOptions.Builder()
                .setPopUpTo(
                    R.id.hSetupFragment,
                    true
                )
                .build()
            findNavController().navigate(
                R.id.action_hSetupFragment_to_hRunFragment,
                savedInstanceState,
                hNavOptions
            )
        }
        hSetupListeners()

    }

    private fun hSetupListeners() {
        tvContinue.setOnClickListener {
            if (hSaveDataToPrefs()) {
                findNavController().navigate(R.id.action_hSetupFragment_to_hRunFragment)
            } else {
                Snackbar.make(
                    requireView(),
                    "Please enter all fields",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun hSaveDataToPrefs(): Boolean {
        val hName = etName.text.toString()
        val hWeight = etWeight.text.toString()

        if (hName.isEmpty() || hWeight.isEmpty()) {
            return false
        }
        hSharedPreferences.edit()
            .putString(Constants.H_KEY_NAME, hName)
            .putFloat(Constants.H_KEY_WEIGHT, hWeight.toFloat())
            .putBoolean(Constants.H_FIRST_TIME, false)
            .apply()

        val hToolbarText = "Lets go $hName"
        requireActivity().tvToolbarTitle.text = hToolbarText
        return true
    }

}