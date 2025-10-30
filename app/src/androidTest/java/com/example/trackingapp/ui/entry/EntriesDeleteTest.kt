package com.example.trackingapp.ui.entry

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackingapp.R
import com.example.trackingapp.data.Entry
import com.example.trackingapp.testutil.InMemoryDbRule
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * UI test covering the deletion flow: click delete, confirm dialog, verify removal.
 */
@RunWith(AndroidJUnit4::class)
class EntriesDeleteTest {

    @get:Rule val dbRule = InMemoryDbRule()

    @Test
    fun delete_removesItemFromList() {
        // Seed the DB before launching the screen
        runBlocking {
            dbRule.db.entryDao().insert(
                Entry(date = LocalDate.now(), amount = 150, note = "delete me")
            )
        }

        ActivityScenario.launch(EntriesActivity::class.java)

        // Ensure the seeded row is visible
        onView(withText("delete me")).check(matches(isDisplayed()))

        // Click the delete button in the first row
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<EntriesAdapter.VH>(
                0, MyViewActions.clickChildViewWithId(R.id.btnDelete)
            )
        )

        // Confirm in dialog
        onView(withText(R.string.delete))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        // Verify removal
        onView(withText("delete me")).check(doesNotExist())
    }
}
