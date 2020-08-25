/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp

import android.app.Application
import com.hashim.runningapp.utils.Constants
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/*To make it a hilt class*/
@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        hInitTimber()
    }

    private fun hInitTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, java.lang.String.format(Constants.hTag, tag), message, t)
                }
            })
        }
    }
}