package com.gmail.wwon.seokk.imagestorage.data.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.util.*

class Converters {

    // Timestamp -> Date 변환
    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    // Date -> Timestamp 변환
    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }
}