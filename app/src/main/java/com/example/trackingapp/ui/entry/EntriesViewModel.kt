package com.example.trackingapp.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingapp.data.Entry
import com.example.trackingapp.repo.TrackerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Displays a reactive list of all entries and basic modifications.
 * Uses MIN..MAX date bounds to represent “all time”.
 */
class EntriesViewModel(private val repo: TrackerRepository) : ViewModel() {

    /** Live list of all entries as a [StateFlow] for the UI. */
    val entries: StateFlow<List<Entry>> = repo
        .entriesInRange(LocalDate.MIN, LocalDate.MAX)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Deletes [entry] from storage. */
    fun delete(entry: Entry) = viewModelScope.launch {
        repo.deleteEntry(entry)
    }

    /** Updates [entry] fields. */
    fun update(entry: Entry) = viewModelScope.launch {
        repo.update(entry)
    }
}

