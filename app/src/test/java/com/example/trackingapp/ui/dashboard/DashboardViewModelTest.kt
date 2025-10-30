package com.example.trackingapp.ui.dashboard
import com.example.trackingapp.MainDispatcherRule
import com.example.trackingapp.data.Goal
import com.example.trackingapp.repo.TrackerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repo: TrackerRepository = mock()

    @Test
    fun uiState_reflects_repo_data() = runTest {
        // Arrange: stub repo flows
        val goalFlow = MutableStateFlow(Goal(id = 0, target = 900))
        val totalFlow = MutableStateFlow(100)

        whenever(repo.observeGoal()).thenReturn(goalFlow)
        whenever(repo.totalInRange(any(), any())).thenReturn(totalFlow)

        val vm = DashboardViewModel(repo)

        // the dispatcher run queued coroutines
        advanceUntilIdle()


        val state = vm.uiState.drop(1).first()  // skip default initial state

        // Assert
        assertEquals(900, state.target)  // comes from Goal(target=900)
        assertEquals(11, state.percent)  // 100 / 900 = 11%
    }
}