package com.example.trackingapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingapp.domain.percentComplete
import com.example.trackingapp.repo.TrackerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

/** Immutable state for the dashboard progress UI. */
data class DashboardUiState(
    val total: Int = 0,
    val target: Int = 0,
    val percent: Int = 0,
    val showHours: Boolean = false,
    val achieved: Boolean = false,       // reached or exceeded target
    val exceededPercent: Int = 0         // amount beyond 100%
)

/**
 * Aggregates weekly totals and goal into [DashboardUiState].
 * Automatically clearing the goal once it has been achieved.
 */
class DashboardViewModel(private val repo: TrackerRepository) : ViewModel() {

    private val autoCleared = MutableStateFlow(false)
    private val showHoursFlow = MutableStateFlow(false)

    /** Monday of the week for [date]. */
    private fun weekStart(date: LocalDate = LocalDate.now()): LocalDate =
        date.with(DayOfWeek.MONDAY)

    /** Sunday of the same week. */
    private fun weekEnd(date: LocalDate = LocalDate.now()): LocalDate =
        weekStart(date).plusDays(6)

    /** Combined stream of weekly totals, goal, and UI toggles. */
    val uiState: StateFlow<DashboardUiState> = combine(
        repo.observeGoal(),
        repo.totalInRange(weekStart(), weekEnd()),
        autoCleared,
        showHoursFlow
    ) { goal, total, _, showHours ->
        val target = goal?.target ?: 0
        val achieved = target > 0 && total >= target
        val exceededPct = if (achieved) (((total - target) * 100.0) / target).toInt() else 0

        // Automatically clearing the goal once it has been achieved.
        if (achieved && !autoCleared.value) {
            viewModelScope.launch {
                repo.clearGoal()
                autoCleared.value = true
            }
        }

        DashboardUiState(
            total = total,
            target = target,
            percent = percentComplete(total, target),
            showHours = showHours,
            achieved = achieved,
            exceededPercent = exceededPct
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DashboardUiState()
    )

    /** Toggles between minutes and “Hh Mm” display. */
    fun toggleUnits() { showHoursFlow.value = !showHoursFlow.value }

    /** Hook for manual refresh if needed later. */
    fun refresh() { viewModelScope.launch { } }

    /** Clears all application data and resets temporary user interface flags. */
    fun resetAll() = viewModelScope.launch {
        repo.resetAll()
        autoCleared.value = false
    }
}
