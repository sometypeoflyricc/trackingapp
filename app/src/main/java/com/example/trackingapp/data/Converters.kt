package com.example.trackingapp.data

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Stored representation:
 * - [LocalDate] <-> epoch day [Long].
 */

class Converters {

    /** Converts nullable epoch day to [LocalDate]. */
    @TypeConverter fun fromEpochDay (epoch: Long?) : LocalDate? = epoch?.let(LocalDate::ofEpochDay)

    /** Converts nullable [LocalDate] to epoch day [Long]. */
    @TypeConverter fun toEpochDay (date : LocalDate?) : Long? = date?.toEpochDay()

}