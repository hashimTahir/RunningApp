/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hashim.runningapp.R
import com.hashim.runningapp.viewmodels.StatsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StatsFragment : Fragment(R.layout.fragment_stats) {
    private val hStatsViewModel: StatsViewModel by viewModels()


}