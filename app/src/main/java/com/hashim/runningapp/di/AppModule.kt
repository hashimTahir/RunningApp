/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.hashim.runningapp.db.RunDao
import com.hashim.runningapp.db.RunningDatabase
import com.hashim.runningapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
/*Appcation component = as the app lives
* other are FragmentComponet,
* ActivityComponent,
* serviceComponent*/
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun hProvidesRunningDatabase(
        @ApplicationContext app: Context
    ): RunningDatabase {
        return Room.databaseBuilder(
            app,
            RunningDatabase::class.java,
            Constants.H_DATABASE_NAME
        )
            .build()
    }

    /*So that we can access only dao but not the database*/
    @Singleton
    @Provides
    fun hProvideRunDao(database: RunningDatabase): RunDao {
        return database.hGetRunDao()
    }

    @Singleton
    @Provides
    fun hProvidesSharedPrefs(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences(
            Constants.H_SHARED_PREFS_NAME,
            MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun hProvidesNamePref(sharedPreferences: SharedPreferences): String? {
        return sharedPreferences.getString(Constants.H_KEY_NAME, "") ?: ""
    }


    @Singleton
    @Provides
    fun hProvidesWeightPref(sharedPreferences: SharedPreferences): Float {
        return sharedPreferences.getFloat(Constants.H_KEY_WEIGHT, 80F)
    }

    @Singleton
    @Provides
    fun hProvidesFirstTimePref(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(Constants.H_FIRST_TIME, true)
    }
}