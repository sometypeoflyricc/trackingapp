package com.example.trackingapp.ui.entry

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.R
import com.example.trackingapp.databinding.ActivityEntriesBinding
import com.example.trackingapp.repo.ServiceLocator
import com.example.trackingapp.repo.TrackerRepository
import com.example.trackingapp.ui.common.EntriesVMFactory
import kotlinx.coroutines.launch

/**
 * A screen with a list displaying all entries with the option to edit/delete them.
 * Drives a [RecyclerView] adapter and observes a reactive list from the VM.
 */
class EntriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntriesBinding

    /** ViewModel displaying a stateful list of all records */
    private val viewModel: EntriesViewModel by viewModels {
        val repo = TrackerRepository(ServiceLocator.db(this))
        EntriesVMFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEntriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar Up navigation
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // RecyclerView adapter, delegates edit or delete callbacks back into this Activity
        val adapter = EntriesAdapter(
            onDelete = { entry ->
                // Confirm destructive action with a Material dialog
                AlertDialog.Builder(this)
                    .setTitle(R.string.delete_entry_title)
                    .setMessage(getString(R.string.delete_entry_message, entry.amount))
                    .setPositiveButton(R.string.delete) { _, _ -> viewModel.delete(entry) }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            },
            onEdit = { entry ->
                // Navigate to EditEntryActivity, and passing current values as Intent extras
                startActivity(
                    Intent(this, EditEntryActivity::class.java)
                        .putExtra("entry_id", entry.id)
                        .putExtra("amount", entry.amount)
                        .putExtra("note", entry.note)
                        .putExtra("date", entry.date.toString())
                )
            }
        )

        // RecyclerView setup
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Collect the list while STARTED, submit to the adapter whenever it updates
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.entries.collect { adapter.submitList(it) }
            }
        }
    }
}
