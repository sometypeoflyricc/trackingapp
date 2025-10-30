package com.example.trackingapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** DAO for [Entry]; flows emit on Room dispatcher; date ranges are inclusive. */
@Dao
interface EntryDao {

    /**
     * Inserts a new [Entry].
     *
     * @return id of the inserted row.
     */
    @Insert
    suspend fun insert(entry: Entry): Long

    /** Updates an existing [Entry] by primary key if it exists. */
    @Update
    suspend fun update(entry: Entry)

    /** Deletes the given [Entry] if it exists. */
    @Delete
    suspend fun delete(entry: Entry)

    /** Entries with date in [start, end], newest first. */
    @Query("SELECT * FROM Entry WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun entriesBetween(start: LocalDate, end: LocalDate): Flow<List<Entry>>

    /** Sum of amount for dates in [start, end] (0 if none). */
    @Query("SELECT IFNULL(SUM(amount), 0) FROM Entry WHERE date BETWEEN :start AND :end")
    fun totalBetween(start: LocalDate, end: LocalDate): Flow<Int>

    /** Deletes all entries. */
    @Query("DELETE FROM Entry")
    suspend fun clearAll()
}
