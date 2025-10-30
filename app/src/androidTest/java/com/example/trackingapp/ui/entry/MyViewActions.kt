package com.example.trackingapp.ui.entry

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import org.hamcrest.Matcher

/**
 * Custom Espresso action to click a child view inside a RecyclerView row.
 */
object MyViewActions {
    /** Clicks the child view with [id] within the acting parent view. */
    fun clickChildViewWithId(@IdRes id: Int): ViewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewActions.click().constraints
        override fun getDescription() = "Click child view with id $id"
        override fun perform(uiController: UiController, view: View) {
            view.findViewById<View>(id).performClick()
        }
    }
}
