package com.example.trackingapp.ui.dashboard

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.trackingapp.R
import com.example.trackingapp.data.Goal
import com.example.trackingapp.testutil.InMemoryDbRule
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation tests for [DashboardActivity].
 *
 * Verification:
 * When no goal exists, the screen shows an empty state and hides summary/progress.
 * When a goal exists, summary/progress are visible and non-empty.
 *
 *
 * [InMemoryDbRule] injects an isolated in-memory Room DB so tests do not
 *   touch device data and each test starts from a clean slate.
 */
@RunWith(AndroidJUnit4::class)
class DashboardActivityTest {

    /** Isolated in-memory DB injected into the app via ServiceLocator for each test. */
    @get:Rule
    val dbRule = InMemoryDbRule()

    /**
     * Launches the dashboard with an empty DB.
     *
     * Expected:
     * `emptyState` is visible to guide the user to set a goal.
     * `summary` and `progress` are hidden (no goal to compare against).
     *  Action buttons remain visible so the user can proceed.
     */
    @Test
    fun showsEmptyState_whenNoGoal() {
        // DB starts empty
        ActivityScenario.launch(DashboardActivity::class.java)

        onView(withId(R.id.emptyState)).check(matches(isDisplayed()))
        onView(withId(R.id.summary)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        // Buttons are visible regardless
        onView(withId(R.id.btnLog)).check(matches(isDisplayed()))
        onView(withId(R.id.btnGoal)).check(matches(isDisplayed()))
        onView(withId(R.id.btnViewEntries)).check(matches(isDisplayed()))
        onView(withId(R.id.btnReset)).check(matches(isDisplayed()))
    }

    /**
     * Sets a goal and relaunches the dashboard.
     *
     * Expected:
     * `emptyState` is hidden.
     * `summary` and `progress` are visible.
     * `summary` text is non-empty, value depends on live totals.
     */
    @Test
    fun showsSummaryAndProgress_whenGoalExists() {
        // Pre-populate a goal (suspend DAO call)
        runBlocking {
            dbRule.db.goalDao().upsert(Goal(id = 0, target = 900))
        }

        ActivityScenario.launch(DashboardActivity::class.java)

        onView(withId(R.id.emptyState)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.summary)).check(matches(isDisplayed()))
        onView(withId(R.id.progress)).check(matches(isDisplayed()))

        // Sanity: summary text exists and is not empty
        onView(withId(R.id.summary)).check(matches(not(withText(""))))
    }
}
