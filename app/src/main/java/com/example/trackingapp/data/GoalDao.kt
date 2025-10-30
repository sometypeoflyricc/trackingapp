package com.example.trackingapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** DAO for single-row [Goal] (id=0). */
@Dao
interface GoalDao {

    /** Emits current goal or null. */
    @Query("SELECT * FROM Goal WHERE id = 0")
    fun observeGoal(): Flow<Goal?>

    /** Upserts goal (id=0). */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert (goal: Goal)

    /** Clears the goal row. */
    @Query("DELETE FROM Goal WHERE id = 0")
    suspend fun clearGoal()
}