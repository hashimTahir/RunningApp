/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hashim.runningapp.R
import kotlinx.android.synthetic.main.fragment_setup.*


class SetupFragment : BaseFragment(R.layout.fragment_setup) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hSetupListeners()

    }

    private fun hSetupListeners() {
        tvContinue.setOnClickListener {
            findNavController().navigate(R.id.action_hSetupFragment_to_hRunFragment)
        }
    }


}