package com.example.trackingapp.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeUtilsTest {

    /** Unit tests for time formatting in minutes vs. “Hh Mm”. */
    @Test
    fun formatTime_minutes_only() {
        // 155 minutes -> "155 min"
        assertEquals("155 min", formatTime(totalMinutes = 155, asHours = false))
    }

    @Test
    fun formatTime_hours_and_minutes_0m() {
        // 180 minutes -> "3h 0m"
        assertEquals("3h 0m", formatTime(totalMinutes = 180, asHours = true))
    }

    @Test
    fun formatTime_hours_and_minutes_with_remainder() {
        // 95 minutes -> "1h 35m"
        assertEquals("1h 35m", formatTime(totalMinutes = 95, asHours = true))
    }

    @Test
    fun formatTime_zero() {
        // 0 minutes -> "0m"
        assertEquals("0m", formatTime(totalMinutes = 0, asHours = true))
    }
}