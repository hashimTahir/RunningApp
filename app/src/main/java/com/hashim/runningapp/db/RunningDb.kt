/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


/*Dagger will handle the singleton functionality of the database*/
@Database(
    entities = [Run::class],
    version = 1,
)
@TypeConverters(TypeConvertors::class)
abstract class RunningDb : RoomDatabase() {
    abstract fun hGetRunDao(): RunDao

}