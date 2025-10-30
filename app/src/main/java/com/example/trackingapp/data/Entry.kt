package com.example.trackingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Tracking app entry persisted in the local database.
 *
 * @property id auto-generated primary key.
 * @property date calendar date the entry belongs to.
 * @property amount amount of minutes during the studying session.
 * @property note optional description of studying session.
 */
@Entity
data class Entry (
@PrimaryKey(autoGenerate = true) val id: Long = 0,
val date: LocalDate,
val amount: Int,
val note: String?= null
){}