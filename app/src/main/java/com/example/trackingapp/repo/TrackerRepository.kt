package com.example.trackingapp.repo

import com.example.trackingapp.data.AppDatabase
import com.example.trackingapp.data.Entry
import com.example.trackingapp.data.Goal
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** High-level API over DAOs; flows are inclusive on date ranges. */
class TrackerRepository(private val db: AppDatabase) {

    // Goal
    /** Observes the current [Goal], or null if not set. */
    fun observeGoal(): Flow<Goal?> = db.goalDao().observeGoal()

    /** Sets or updates the goal to [target]. */
    suspend fun setGoal(target: Int) = db.goalDao().upsert(Goal(id = 0, target = target))

    /** Clears the goal row, if it exists. */
    suspend fun clearGoal() = db.goalDao().clearGoal()

    // Entries
    /** Streams entries with dates in [start, end] inclusive, newest first. */
    fun entriesInRange(start: LocalDate, end: LocalDate): Flow<List<Entry>> =
        db.entryDao().entriesBetween(start, end)

    /** Streams the total of [Entry.amount] for entries dated in [start, end] inclusive. */
    fun totalInRange(start: LocalDate, end: LocalDate): Flow<Int> =
        db.entryDao().totalBetween(start, end)

    /** Adds a new entry with the given fields. */
    suspend fun addEntry(date: LocalDate, amount: Int, note: String?) {
        db.entryDao().insert(Entry(date = date, amount = amount, note = note))
    }

    /** Updates an existing [Entry] if the row exists. */
    suspend fun update(entry: Entry) = db.entryDao().update(entry)

    /** Deletes the given [Entry] if the row exists. */
    suspend fun deleteEntry(entry: Entry) = db.entryDao().delete(entry)

    // Reset
    /** Clears all persisted data (both entries and goal). */
    suspend fun resetAll() {
        db.entryDao().clearAll()
        db.goalDao().clearGoal()
    }
}
