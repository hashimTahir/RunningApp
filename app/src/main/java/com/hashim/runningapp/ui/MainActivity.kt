/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hashim.runningapp.R
import com.hashim.runningapp.db.RunDao
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var hRunDao: RunDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.d("Runn Dao ${hRunDao.hashCode()}")
    }
}