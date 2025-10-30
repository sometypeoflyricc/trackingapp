package com.example.trackingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single row table storing current goal of user.
 *
 * @property id always 0 for the only row in the table.
 * @property target target value.
 */
@Entity
data class Goal(
    @PrimaryKey val id: Int=0,
    val target: Int
) {
}