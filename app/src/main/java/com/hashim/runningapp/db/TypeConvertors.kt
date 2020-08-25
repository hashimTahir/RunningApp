/*
 * Copyright (c) 2020/  8/ 25.  Created by Hashim Tahir
 */

package com.hashim.runningapp.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class TypeConvertors {
    @TypeConverter
    fun hFromBitmap(bitmap: Bitmap): ByteArray {
        val hOutputStream = ByteArrayOutputStream()
        bitmap.compress(
            Bitmap.CompressFormat.PNG,
            100,
            hOutputStream
        )
        return hOutputStream.toByteArray()
    }

    @TypeConverter
    fun hToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(
            byteArray,
            0,
            byteArray.size
        )
    }
}