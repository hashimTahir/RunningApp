/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hashim.runningapp.db.Run
import com.hashim.runningapp.repository.local.LocalRepo
import com.hashim.runningapp.utils.SortType.*
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val hLocalRepo: LocalRepo
) : ViewModel() {

    private val hRunsSortedByDate = hLocalRepo.hGetAllRunsSortedByDate()
    private val hRunsSortedByDistance = hLocalRepo.hGetAllRunsSortedByDistanceInMeters()
    private val hRunsSortedByCaloriesBurnt = hLocalRepo.hGetAllRunsSortedByCaloriesBurnt()
    private val hRunsSortedByTimeinMillis = hLocalRepo.hGetAllRunsSortedByTimeInMills()
    private val hRunsSortedByAverageSpeed = hLocalRepo.hGetAllRunsSortedByAverageSpeed()

    private val hSortType = H_DATE

    /*Mediator live data allows to merge several live datas togather
    * also allows to write custom logic to emit what data is to be emitted.*/
    val hMediatorLiveData = MediatorLiveData<List<Run>>()

    init {
        /*For any change in the source change lambda is called*/
        hMediatorLiveData.addSource(hRunsSortedByDate) { result ->
            if (hSortType.equals(H_DATE)) {
                result?.let {
                    hMediatorLiveData.value = result
                }
            }
        }

        hMediatorLiveData.addSource(hRunsSortedByDistance) { result ->
            if (hSortType.equals(H_DISTANCE)) {
                result?.let {
                    hMediatorLiveData.value = result
                }
            }
        }
        hMediatorLiveData.addSource(hRunsSortedByCaloriesBurnt) { result ->
            if (hSortType.equals(H_CALORIES_BURNED)) {
                result?.let {
                    hMediatorLiveData.value = result
                }
            }
        }
        hMediatorLiveData.addSource(hRunsSortedByTimeinMillis) { result ->
            if (hSortType.equals(H_RUNNING_TIME)) {
                result?.let {
                    hMediatorLiveData.value = result
                }
            }
        }
        hMediatorLiveData.addSource(hRunsSortedByAverageSpeed) { result ->
            if (hSortType.equals(H_AVG_SPEED)) {
                result?.let {
                    hMediatorLiveData.value = result
                }
            }

        }
    }

    fun hInsertRun(run: Run) {
        viewModelScope.launch {
            hLocalRepo.hInsertRun(run)
        }
    }

}