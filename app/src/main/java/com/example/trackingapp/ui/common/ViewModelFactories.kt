package com.example.trackingapp.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackingapp.repo.TrackerRepository
import com.example.trackingapp.ui.dashboard.DashboardViewModel
import com.example.trackingapp.ui.entry.EntryViewModel
import com.example.trackingapp.ui.entry.EntriesViewModel
import com.example.trackingapp.ui.goal.GoalViewModel

/** Creates a [DashboardViewModel] with the provided [TrackerRepository]. */
class DashboardVMFactory(private val repo: TrackerRepository) : ViewModelProvider.Factory {
    /** Returns a [DashboardViewModel] or throws if [modelClass] isn’t supported. */
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
}

/** Creates a [GoalViewModel] with the provided [TrackerRepository]. */
class GoalVMFactory(private val repo: TrackerRepository) : ViewModelProvider.Factory {
    /** Returns a [GoalViewModel] or throws if [modelClass] isn’t supported. */
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(GoalViewModel::class.java) ->
                GoalViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
}

/** Creates an [EntryViewModel] with the provided [TrackerRepository]. */
class EntryVMFactory(private val repo: TrackerRepository) : ViewModelProvider.Factory {
    /** Returns an [EntryViewModel] or throws if [modelClass] isn’t supported. */
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(EntryViewModel::class.java) ->
                EntryViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
}

/** Creates an [EntriesViewModel] with the provided [TrackerRepository]. */
class EntriesVMFactory(private val repo: TrackerRepository) : ViewModelProvider.Factory {
    /** Returns an [EntriesViewModel] or throws if [modelClass] isn’t supported. */
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(EntriesViewModel::class.java) ->
                EntriesViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
}