package com.example.trackingapp.repo

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.trackingapp.data.AppDatabase

/**
 * Simple service locator for obtaining the app's [AppDatabase] instance.
 *
 * Returns a file based Room database named "tracker.db".
 * Double-checked locking around the production DB field.
 * [testDb] is guarded and cannot be set by app code accidentally.
 */
object ServiceLocator {

    // File based DB
    @Volatile
    private var prodDb: AppDatabase? = null

    // Test hook: when set from androidTest, this is returned instead of prodDb
    @Volatile
    @VisibleForTesting
    var testDb: AppDatabase? = null
        private set // prevent app code from setting it accidentally

    /**
     * Returns the active database instance.
     *
     * Resolution order:
     * 1) If a test DB is set, return it.
     * 2) Else lazily build/open the production DB (singleton).
     */
    fun db(context: Context): AppDatabase {
        // If a test DB is supplied, always use it
        testDb?.let { return it }

        // Otherwise use/create the production DB
        return prodDb ?: synchronized(this) {
            prodDb ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tracker.db"
            ).build().also { prodDb = it }
        }
    }

    /** In androidTest replaces the active DB with a test instance or clears it. */
    @VisibleForTesting
    fun setTestDb(db: AppDatabase?) {
        testDb = db
    }

    /** Resets both prod and test DB references. */
    @VisibleForTesting
    fun clearForTests() {
        prodDb = null
        testDb = null
    }
}
