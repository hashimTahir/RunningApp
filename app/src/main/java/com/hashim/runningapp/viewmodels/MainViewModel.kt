/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hashim.runningapp.db.Run
import com.hashim.runningapp.repository.local.LocalRepo
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val hLocalRepo: LocalRepo
) : ViewModel() {

    val hRunsSortedByDate = hLocalRepo.hGetAllRunsSortedByDate()

    fun hInsertRun(run: Run) {
        viewModelScope.launch {
            hLocalRepo.hInsertRun(run)
        }
    }

}