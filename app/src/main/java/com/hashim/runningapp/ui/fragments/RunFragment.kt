/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hashim.runningapp.R
import kotlinx.android.synthetic.main.fragment_run.*


class RunFragment : BaseFragment(R.layout.fragment_run) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hSetupListeners()
    }

    private fun hSetupListeners() {
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_hRunFragment_to_hTrackingFragment)
        }
    }

}