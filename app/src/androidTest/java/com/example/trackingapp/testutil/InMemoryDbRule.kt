package com.example.trackingapp.testutil

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.trackingapp.data.AppDatabase
import com.example.trackingapp.repo.ServiceLocator
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * JUnit rule providing an in-memory Room DB and wiring it into [ServiceLocator].
 * Ensures UI tests run against isolated state and cleans up afterward.
 */
class InMemoryDbRule : TestRule {
    /** The active in-memory DB instance for seeding assertions. */
    lateinit var db: AppDatabase
        private set

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                val ctx = ApplicationProvider.getApplicationContext<Context>()
                db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
                    .allowMainThreadQueries() // acceptable for tests
                    .build()

                // Make the app use in-memory DB
                ServiceLocator.setTestDb(db)

                try {
                    base.evaluate()
                } finally {
                    db.close()
                    ServiceLocator.clearForTests()
                }
            }
        }
    }
}
