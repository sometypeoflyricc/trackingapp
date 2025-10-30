package com.example.trackingapp.ui.entry

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackingapp.R
import com.example.trackingapp.data.Entry
import com.example.trackingapp.testutil.InMemoryDbRule
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.StringContains.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * UI tests for editing an entry from the list screen.
 * Preloads data, opens edit, modifies amount, and verifies the updated list.
 *
 * Uses [InMemoryDbRule] so each test starts with a clean Room DB wired into the app.
 */
@RunWith(AndroidJUnit4::class)
class EntriesActivityTest {

    /** In-memory DB injected through the ServiceLocator for isolation. */
    @get:Rule
    val dbRule = InMemoryDbRule()

    /**
     * Given a seeded entry, when the first row is tapped,
     * then EditEntryActivity opens and shows its fields.
     */
    @Test
    fun tapRow_opensEditScreen() {
        // Seed one entry directly via DAO
        runBlocking {
            dbRule.db.entryDao().insert(
                Entry(date = LocalDate.now(), amount = 120, note = "seed")
            )
        }

        // Launch the list
        ActivityScenario.launch(EntriesActivity::class.java)

        // Click the first row in the RecyclerView
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<EntriesAdapter.VH>(
                    0, click()
                )
            )

        // The editing screen is displayed with the amount pre-filled.
        onView(withId(R.id.btnUpdate)).check(matches(isDisplayed()))
        onView(withId(R.id.etAmount)).check(matches(withText("120")))
    }

    /**
     * Given a seeded entry, when it is opened, the amount is changed to 200, and Update is pressed,
     * then the list reflects "200 min" for that row.
     */
    @Test
    fun edit_updatesEntryInList() {
        // Seed the DB
        runBlocking {
            dbRule.db.entryDao().insert(
                Entry(date = LocalDate.now(), amount = 100, note = "to edit")
            )
        }

        ActivityScenario.launch(EntriesActivity::class.java)

        // Open the first item
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<EntriesAdapter.VH>(
                    0, click()
                )
            )

        // Replace the amount and save
        onView(withId(R.id.etAmount)).perform(replaceText("200"), closeSoftKeyboard())
        onView(withId(R.id.btnUpdate)).perform(click())

        // In the list item text shows updated minutes
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withText(containsString("200 min"))).check(matches(isDisplayed()))
    }
}
