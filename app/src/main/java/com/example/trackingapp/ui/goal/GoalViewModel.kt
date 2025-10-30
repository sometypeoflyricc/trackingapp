package com.example.trackingapp.ui.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingapp.repo.TrackerRepository
import kotlinx.coroutines.launch

/** Stores the value of a single goal through a repository.  */
class GoalViewModel(private val repo: TrackerRepository) : ViewModel() {

    /** Saves a positive [target] as the current goal. */
    fun saveGoal(target: Int) = viewModelScope.launch { repo.setGoal(target) }
}
