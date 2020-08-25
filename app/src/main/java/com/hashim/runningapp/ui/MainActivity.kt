/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.hashim.runningapp.R
import com.hashim.runningapp.db.RunDao
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var hRunDao: RunDao

    private lateinit var hNavHostFragment: NavHostFragment
    private lateinit var hNavController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.d("Runn Dao ${hRunDao.hashCode()}")

        setSupportActionBar(toolbar)
        hInitNavHostFragment()

    }

    private fun hInitNavHostFragment() {
        hNavHostFragment = supportFragmentManager
            .findFragmentById(R.id.hNavHostFragment) as NavHostFragment

        hNavController = hNavHostFragment.navController
        hNavController.setGraph(R.navigation.home_nav_graph)
        NavigationUI.setupWithNavController(bottomNavigationView, hNavController)

        hNavController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.hSettingsFragment, R.id.hRunFragment, R.id.hStatsFragment ->
                    bottomNavigationView.visibility = View.VISIBLE
                else ->
                    bottomNavigationView.visibility = View.GONE
            }
        }
    }


}