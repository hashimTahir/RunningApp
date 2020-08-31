/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.hashim.runningapp.R
import com.hashim.runningapp.db.RunDao
import com.hashim.runningapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var hRunDao: RunDao

    private lateinit var hNavHostFragment: NavHostFragment
    private lateinit var hNavController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Timber.d("Runn Dao ${hRunDao.hashCode()}")

        hNavigationToTrackingFragment(intent)

        hInitNavHostFragment()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        hNavigationToTrackingFragment(intent)
    }

    private fun hInitNavHostFragment() {
        hNavHostFragment = supportFragmentManager
            .findFragmentById(R.id.hNavHostFragment) as NavHostFragment

        hNavController = hNavHostFragment.navController
        hNavController.setGraph(R.navigation.home_nav_graph)
        NavigationUI.setupWithNavController(bottomNavigationView, hNavController)
        bottomNavigationView.setOnNavigationItemReselectedListener {
            /*No-Op*/
        }

        hNavController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.hSettingsFragment, R.id.hRunFragment, R.id.hStatsFragment ->
                    bottomNavigationView.visibility = View.VISIBLE
                else ->
                    bottomNavigationView.visibility = View.GONE
            }
        }
    }

    private fun hNavigationToTrackingFragment(intent: Intent?) {
        if (intent?.action.equals(Constants.H_ACTION_SHOW_TRACKING_FRAGMENT)) {
            hNavController.navigate(R.id.action_global_tracking_fragment)
        }
    }


}