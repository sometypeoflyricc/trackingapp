package com.example.trackingapp.domain

/**
 * Formats the duration specified in minutes.
 *
 * @param totalMinutes duration in minutes (non-negative).
 * @param asHours if true, returns a compact "Hh Mm" string. Else "X min".
 */
fun formatTime(totalMinutes: Int, asHours: Boolean): String {
    return if (asHours) {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    } else {
        "$totalMinutes min"
    }
}