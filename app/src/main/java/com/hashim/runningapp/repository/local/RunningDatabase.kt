/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


/*Dagger will handle the singleton functionality of the database*/
@Database(
    entities = [Run::class],
    version = 1,
)
@TypeConverters(TypeConvertors::class)
abstract class RunningDatabase : RoomDatabase() {
    abstract fun hGetRunDao(): RunDao

}