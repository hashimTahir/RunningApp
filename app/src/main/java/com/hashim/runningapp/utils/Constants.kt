/*
 * Copyright (c) 2020/  8/ 18.  Created by Hashim Tahir
 */

package com.hashim.runningapp.utils

import android.graphics.Color

class Constants {
    companion object {
        val H_LOACTION_RC = 23
        const val hTag = "hashimTimberTags %s"
        const val H_DATABASE_NAME = "RunningDatabase"


        const val H_LOCATION_UPDATE_INTERVAL = 5000L
        const val H_FATEST_LOCATION_UPDATE_INTERVAL = 2000L


        const val H_ACTION_START_OR_RESUME = "H_ACTION_START_OR_RESUME"
        const val H_ACTION_PAUSE_SERVICE = "H_ACTION_PAUSE_SERVICE"
        const val H_ACTION_STOP_SERVICE = "H_ACTION_STOP_SERVICE"
        const val H_ACTION_SHOW_TRACKING_FRAGMENT = "H_ACTION_SHOW_TRACKING_FRAGMENT"


        const val H_POLYLINE_COLOR = Color.RED
        const val H_POLYLINE_WIDTH = 8F
        const val H_CAMERA_ZOOM = 15F
        const val H_DELAY_INTERVAL = 100L


    }
}