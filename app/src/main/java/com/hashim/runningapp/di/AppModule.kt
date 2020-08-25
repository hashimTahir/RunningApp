/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.di

import android.content.Context
import androidx.room.Room
import com.hashim.runningapp.repository.local.RunDao
import com.hashim.runningapp.repository.local.RunningDatabase
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
}