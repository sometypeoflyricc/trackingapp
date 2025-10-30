package com.example.trackingapp.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackingapp.data.Entry
import com.example.trackingapp.repo.TrackerRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

/** ViewModel for creating, updating, and deleting [Entry] records. */
class EntryViewModel(private val repo: TrackerRepository) : ViewModel() {

    /** Adds a new entry for [date] with [amount] and optional [note]. */
    fun add(date: LocalDate, amount: Int, note: String?) =
        viewModelScope.launch { repo.addEntry(date, amount, note) }

    /** Updates an existing [entry]. */
    fun update(entry: Entry) =
        viewModelScope.launch { repo.update(entry) }

    /** Deletes [entry]. */
    fun delete(entry: Entry) =
        viewModelScope.launch { repo.deleteEntry(entry) }
}
