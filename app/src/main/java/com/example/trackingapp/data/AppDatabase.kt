package com.example.trackingapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/** Room DB for entries and the single-row goal; uses [Converters] for LocalDate. */
@Database(entities = [Entry::class, Goal::class], version=1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /** DAO for [Entry] CRUD and aggregations. */
    abstract fun entryDao(): EntryDao

    /** DAO for single-row [Goal] operations. */
    abstract fun goalDao(): GoalDao
}