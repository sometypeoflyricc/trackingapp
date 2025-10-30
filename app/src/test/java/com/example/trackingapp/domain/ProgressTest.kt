package com.example.trackingapp.domain

import org.junit.Assert.assertEquals
import org.junit.Test

/** Unit tests for calculating percentage progress and limitations.  */
class ProgressTest {
    @Test fun percent_isZero_whenTargetZero() {
        assertEquals(0, percentComplete(total = 200, target = 0))
    }
    @Test fun percent_isZero_whenTotalZero() {
        assertEquals(0, percentComplete(total = 0, target = 500))
    }
    @Test fun percent_roundsDown() {
        assertEquals(33, percentComplete(total = 199, target = 600))
    }
    @Test fun percent_capsAt100() {
        assertEquals(100, percentComplete(total = 1200, target = 600))
    }
}
