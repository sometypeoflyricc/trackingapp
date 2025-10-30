package com.example.trackingapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * JUnit rule that swaps the main dispatcher for a [TestDispatcher].
 * Ensures deterministic coroutine scheduling in unit tests.
 */
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestRule {
    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            Dispatchers.setMain(testDispatcher)
            try {
                base.evaluate()
            } finally {
                Dispatchers.resetMain()
            }
        }
    }
}